package org.cunxin.db.mongodb;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import org.cunxin.db.base.AbstractDBAdapter;
import org.cunxin.db.base.action.Executable;
import org.cunxin.db.base.action.Executable2;
import org.cunxin.db.base.action.QueryGenerator;
import org.cunxin.db.mongodb.MongoAction.EnsureIndex;
import org.cunxin.db.mongodb.MongoAction.Find;
import org.cunxin.db.mongodb.MongoAction.Count;
import org.cunxin.db.mongodb.MongoAction.Insert;
import org.cunxin.db.mongodb.MongoAction.Remove;
import org.cunxin.db.mongodb.MongoAction.Update;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.Bytes;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;

public class MongoDBAdapter extends AbstractDBAdapter<MongoConnection, MongoAction, MongoAction, DBObject> {
	MongoConnection _connection;

	public MongoDBAdapter(List<ServerAddress> replicaSet, MongoOptions options) {
		_connection = new MongoConnection(replicaSet, options);
	}

	@Override
	protected void InternalExecuteQuery(MongoConnection conn, MongoAction query,
			QueryGenerator<MongoAction> queryGenerator,
			Executable<DBObject> executable, Map<String,String> queryPara) throws Exception {
		InternalExecuteQuery(conn, query, queryGenerator, executable);
	}

	@Override
	protected void InternalExecuteQuery(MongoConnection conn, MongoAction query,
			QueryGenerator<MongoAction> queryGenerator,
			Executable<DBObject> executable) throws Exception {
		DBCursor cursor = null;
		try {
			if (query instanceof Find) {
				Find q = (Find)query;
				if (queryGenerator != null) {
					queryGenerator.execute(q);
				}
				cursor = conn.getConnection(q.dbName, q.collectionName).find(q.ref, q.keys);
				if(q.sortBy != null)
					cursor = cursor.sort(q.sortBy);
				if(q.limit != null)
					cursor = cursor.limit(q.limit);
				cursor.addOption(Bytes.QUERYOPTION_NOTIMEOUT);
				while (cursor.hasNext()) {
					executable.execute(cursor.next());
				}
			} else if (query instanceof Count) {
				Count q = (Count)query;
				if (queryGenerator != null) {
					queryGenerator.execute(q);
				}
				Long count = conn.getConnection(q.dbName, q.collectionName).count(q.ref);
				executable.execute(new BasicDBObject("count", count));
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (cursor != null) cursor.close();
			conn.close();
		}
	}

	@Override
	protected <T> Iterator<T> InternalExecuteQuery(final MongoConnection conn,
			MongoAction query, QueryGenerator<MongoAction> queryGenerator,
			final Executable2<? extends T, DBObject> executable)
					throws Exception {
		Find q = (Find)query;
		if (queryGenerator != null) {
			queryGenerator.execute(q);
		}
		DBCursor cursor = conn.getConnection(q.dbName, q.collectionName).find(q.ref, q.keys);
		if(q.sortBy != null)
			cursor = cursor.sort(q.sortBy);
		if(q.limit != null)
			cursor = cursor.limit(q.limit);
		final DBCursor finalCursor = cursor;
		cursor.addOption(Bytes.QUERYOPTION_NOTIMEOUT);
		return new Iterator<T>() {
			private boolean _hasNext = initHasNext();
			private DBCursor _cursor = finalCursor;
			private MongoConnection _adapter = conn;

			private boolean initHasNext() {
				return finalCursor==null?false:finalCursor.hasNext();
			}

			@Override
			public boolean hasNext() {
				return _hasNext;
			}

			@Override
			public T next() {
				T tmp;
				try {
					executable.execute(_cursor.next());
					tmp = executable.getResult();
				} catch (Exception e) {
					e.printStackTrace();
					_hasNext = false;
					close();
					return null;
				}
				_hasNext = _cursor.hasNext();
				if (!_hasNext) {
					close();
				}
				return tmp;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

			private void close() {
				if (_cursor != null) _cursor.close();
				if (_adapter != null) {
					_adapter.close();
					_adapter = null;
				}
			}

			@Override
			protected void finalize() {
				close();
			}
		};
	}

	@Override
	protected void InternalExecuteUpdate(MongoConnection conn, MongoAction query,
			QueryGenerator<MongoAction> queryGenerator)
					throws Exception {
		try {
			if (query instanceof Update) {
				Update update = (Update)query;
				if (queryGenerator != null) {
					queryGenerator.execute(update);
				}
				if (update.o.isPartialObject()) {
					DBObject obj = new BasicDBObject(update.o.toMap());
					DBObject ori = conn.getConnection(query.dbName, query.collectionName).findOne(update.q);
					if (ori != null) {
						for (String key : ori.keySet()) {
							if (!obj.containsField(key)) {
								obj.put(key, ori.get(key));
							}
						}
					} else if (!update.upsert) {
						return;
					}
					update.o = obj;
				}
				conn.getConnection(query.dbName, query.collectionName).update(
						update.q, update.o, update.upsert, update.multi);
			} else if (query instanceof Insert) {
				Insert insert = (Insert)query;
				if (queryGenerator != null) {
					queryGenerator.execute(insert);
				}
				conn.getConnection(query.dbName,
						query.collectionName).insert(insert.objs);
			} else if (query instanceof EnsureIndex) {
				EnsureIndex q = (EnsureIndex)query;
				if (queryGenerator != null) {
					queryGenerator.execute(q);
				}
				conn.getConnection(q.dbName, q.collectionName).ensureIndex(
						q.keys, null, q.unique);
			} else if (query instanceof Remove) {
				Remove q = (Remove)query;
				if (queryGenerator != null) {
					queryGenerator.execute(q);
				}
				conn.getConnection(q.dbName,
						q.collectionName).remove(q.keys);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			conn.close();
		}
	}

	@Override
	public MongoConnection getMasterConnection() {
		return _connection;
	}

	@Override
	public MongoConnection getSlaveConnection() {
		return _connection;
	}

	public static Map<String, MongoDBAdapter> loadMongoConnectionsFromXML(String fileName) throws ParserConfigurationException, SAXException, IOException {
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		MongoParserHelper handler = new MongoParserHelper(new HashMap<String, MongoDBAdapter>());
		parser.parse(new File(fileName), handler);
		return handler.getResult();
	}

	public Set<String> getTables(String dbname) {
		return _connection.getMongo().getDB(dbname).getCollectionNames();
	}

	public Iterator<DBObject> getAllObjects(final String dbname, final String collectionName, final BasicDBObjectBuilder builder, final int cacheSize) {
		return new Iterator<DBObject>() {
			List<DBObject> models = new LinkedList<DBObject>();
			int startId = -1;
			int pos = 0;
			boolean prepared = false;

			private void init(){
				if(!prepared) {
					prepared = true;
					Iterator<DBObject> iter = getAllModels(startId, cacheSize,  dbname,  collectionName,  builder);
					while(iter.hasNext()) {
						models.add(iter.next());
					}
					if(models.size() > 0) {
						startId = (Integer)models.get(models.size() - 1).get("id");
					}
				}
			}
			@Override
			public boolean hasNext() {
				init();
				if(pos >= models.size()) {
					pos = 0;
					models.clear();
					Iterator<DBObject> iter = getAllModels(startId, cacheSize,  dbname,  collectionName,  builder);
					while(iter.hasNext()) {
						models.add(iter.next());
					}
					if(models.size() == 0) {
						return false;
					}
					startId = (Integer)models.get(models.size() - 1).get("id");
					return true;
				}
				return true;
			}

			@Override
			public DBObject next() {
				init();
				if(pos < models.size()) {
					DBObject model = models.get(pos++);
					return model;
				}
				else {
					return null;
				}
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();

			}
		};
	}

	public Iterator<DBObject> getAllModels(int startId, int limit, String dbname, String collectionName, BasicDBObjectBuilder builder) {
		builder.add("id", new BasicDBObject("$gt", startId));
		Find find = new Find(dbname, collectionName, builder.get());
		find.limit = limit;
		find.sortBy = new BasicDBObject("id", 1);
		return this.executeQuery(
				find,
				new Executable2<DBObject, DBObject>(){
					@Override
					protected DBObject generateFromResultSet(DBObject rs) {
						return rs;
					}
				});
	}
}
