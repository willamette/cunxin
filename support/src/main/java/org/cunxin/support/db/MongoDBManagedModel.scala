package org.cunxin.support.db

import java.util.Date
import org.bson.types.ObjectId
import com.mongodb.DBObject
import com.mongodb.util.JSON
import com.mongodb.casbah.{MongoCollection, MongoDB}
import com.mongodb.casbah.commons.MongoDBObject
import org.apache.commons.logging.LogFactory
import org.cunxin.support.util.JsonSerDe

protected case class MongoDBManagedModel[T](id: ObjectId, data: T) extends DBManagedModel[T] {
  def copy(newData: T) = MongoDBManagedModel(id, newData)
}

protected case class SingletonList[A](originalList: List[A]) {
  def uniqueOption: Option[A] = {
    if (originalList.size > 1) throw new IllegalArgumentException("Cannot find the unique result, found: " + originalList)
    originalList.headOption
  }
}

/**
 * Important: If you are changing this class, please make sure to run com.bazaarvoice.ads.app.dao.DbDaoTest
 * testcase manually.
 */
abstract class AbstractMongoDao[T: Manifest](db: MongoDB, val collectionName: String, val version: Int) {
  protected val log = LogFactory.getLog(this.getClass)

  def findAll(): List[DBManagedModel[T]] = findQuery(None)

  protected implicit def listToSingleton(list: List[DBManagedModel[T]]) = SingletonList(list)

  protected def find[A <: String, B <: Any](query: (A, B)*): List[DBManagedModel[T]] = find(MongoDBObject(query: _*))

  protected def find[A <: String, B <: Any](query: List[(A, B)]): List[DBManagedModel[T]] = find(MongoDBObject(query))

  protected def find(query: DBObject, sort: DBObject): List[DBManagedModel[T]] = findQuery(Option(query), Option(sort))

  protected def find(query: DBObject): List[DBManagedModel[T]] = findQuery(Option(query))

  protected val jackson = new JsonSerDe()
  protected val FIELD_ID = "_id"
  protected val FIELD_VERSION = "version"
  protected val FIELD_LASTUPDATEDTIME = "lastupdatedtime"

  protected val collection: MongoCollection = db(collectionName)

  /**
   * AND : all the fields specified in the DBObject
   * OR : use QueryBuilder, E.g. new QueryBuilder().or(MongoDBObject("field" -> "A"), MongoDBObject("field" -> "B")).get
   * see http://www.mongodb.org/display/DOCS/SQL+to+Mongo+Mapping+Chart
   */
  private[this] def findQuery(query: Option[DBObject], sort: Option[DBObject] = None): List[DBManagedModel[T]] = {
    //if a query is not provided, let's load all objects from collection
    val realQuery = query.getOrElse(MongoDBObject())

    val result = collection.find(realQuery)
    val sorted = sort.map(q => result.sort(q)).getOrElse(result)

    sorted.map(dbObject => {
      val id = dbObject.get(FIELD_ID).asInstanceOf[ObjectId]
      val currVersion = dbObject.get(FIELD_VERSION).asInstanceOf[Int]
      dbObject.removeField(FIELD_ID)
      dbObject.removeField(FIELD_VERSION)
      dbObject.removeField(FIELD_LASTUPDATEDTIME)

      new MongoDBManagedModel(id, jackson.deserialize[T](jackson.serialize(dbObject)))
    }).toList
  }

}

trait MongoRWDao[T] {
  self: AbstractMongoDao[T] =>

  protected def ensureIndex[A <: String, B <: Any](key: (A, B), name: String, unique: Boolean) {
    ensureIndex(List(key), name, unique)
  }

  protected def ensureIndex[A <: String, B <: Any](keys: List[(A, B)], name: String, unique: Boolean) {
    collection.ensureIndex(MongoDBObject(keys), name, unique)
  }

  def create(data: T) {
    val col = collection
    col.insert(toDBObject(data))
    verify(col)
  }

  def update(dbModel: DBManagedModel[T]) {
    val mongoDbModel = toMongoDBManagedModel(dbModel)
    val dbObject = toDBObject(mongoDbModel.data)
    dbObject.put(FIELD_ID, mongoDbModel.id)
    val col = collection
    col.save(dbObject)
    verify(col)
  }

  def delete(dbModel: DBManagedModel[T]) {
    val col = collection
    col.remove(MongoDBObject(FIELD_ID -> toMongoDBManagedModel(dbModel).id))
    verify(col)
  }

  private[this] def toMongoDBManagedModel(dbModel: DBManagedModel[T]) = dbModel match {
    case m: MongoDBManagedModel[T] => m
    case _ => throw new IllegalArgumentException("Invalid DBManagedModel type: " + dbModel.getClass)
  }

  private[this] def toDBObject(model: T): DBObject = {
    val dbObject = JSON.parse(jackson.serialize(model)).asInstanceOf[DBObject]
    dbObject.put(FIELD_VERSION, version)
    dbObject.put(FIELD_LASTUPDATEDTIME, new Date())
    dbObject
  }

  private[this] def dbObjectToMap(dbObject: DBObject): Map[String, AnyRef] = {
    dbObject.keySet.collect {
      case (k) => (k -> dbObject.get(k))
    }.toMap
  }

  private[this] def verify(col: MongoCollection) {
    val ex = col.getLastError().getException
    if (ex != null) throw ex
  }
}
