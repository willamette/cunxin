package org.cunxin.reward.app.dao

import com.google.inject.Inject
import com.mongodb.casbah.MongoDB
import org.cunxin.support.db.{MongoRWDao, AbstractMongoDao}
import org.cunxin.reward.app.model.{User, Badger}

class UserDao @Inject()(db: MongoDB) extends AbstractMongoDao[User](db, "user", version = 1)with MongoRWDao[User] {
  ensureIndex("id" -> 1, "idx_id", unique = true)
  def findUserById(id: String) = find("id" -> id).headOption

}