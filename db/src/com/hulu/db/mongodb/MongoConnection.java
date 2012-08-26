package com.hulu.db.mongodb;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;

public class MongoConnection {
	Mongo _mongo;

	private static final class DataCenterDecision {
		private static ReadPreference _inst;

		public static synchronized ReadPreference getReadPreference() {
			if (_inst == null) {
				try {
					String ipAddress = InetAddress.getLocalHost().getHostAddress();
					if (ipAddress != null) {
						//TODO
						if (false) {
							//_inst = new ReadPreference.TaggedReadPreference(new BasicDBObject("dc", "iad"));
							_inst = ReadPreference.SECONDARY;
						} else {
							//_inst = new ReadPreference.TaggedReadPreference(new BasicDBObject("dc", "els"));
							_inst = ReadPreference.PRIMARY;
						}
						return _inst;
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				_inst = ReadPreference.SECONDARY;
			}
			return _inst;
		}
	}

	public MongoConnection(List<ServerAddress> replicaSet, MongoOptions options) {
		_mongo = new Mongo(replicaSet, options);
		if (_mongo.getConnector().getServerAddressList().size() > 0) {
			_mongo.setReadPreference(DataCenterDecision.getReadPreference());
		} else {
			_mongo.setReadPreference(ReadPreference.PRIMARY);
		}
	}

	public DBCollection getConnection(String dbname, String collectionName) {
		return getConnection(dbname, collectionName, null, null,  true);
	}

	public void close() {
		// do nothing
	}

	public DBCollection getConnection(String dbName, String collectionName, String userName, String password, boolean slaveOk) {
		DB db = _mongo.getDB(dbName);
		if(userName != null && password != null)
			db.authenticate(userName, password.toCharArray());
		DBCollection collection = _mongo.getDB(dbName).getCollection(collectionName);
		if (!slaveOk) {
			collection.setReadPreference(ReadPreference.PRIMARY);
		}
		return collection;
	}

	public Mongo getMongo() {
		return _mongo;
	}
}
