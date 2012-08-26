package com.hulu.db.mongodb;

import com.mongodb.DBObject;

public abstract class MongoAction {
	public MongoAction(String dbName, String collectionName) {
		this.dbName = dbName;
		this.collectionName = collectionName;
	}

	public String dbName;
	public String collectionName;

	public static class Update extends MongoAction {
		public DBObject q;
		public DBObject o;
		public boolean upsert;
		public boolean multi;

		public Update(String dbName, String collectionName, DBObject q, DBObject o) {
			this(dbName, collectionName, q, o, true);
		}

		public Update(String dbName, String collectionName, DBObject q, DBObject o, boolean upsert) {
			super(dbName, collectionName);
			this.q = q;
			this.o = o;
			this.upsert = upsert;
			this.multi = false; 
		}
	}

	public static class Find extends MongoAction {
		public Find(String dbName, String collectionName, DBObject ref) {
			this(dbName, collectionName, ref, null);
		}

		public Find(String dbName, String collectionName) {
			this(dbName, collectionName, null, null);
		}

		public Find(String dbName, String collectionName, DBObject ref,
				DBObject keys) {
			super(dbName, collectionName);
			this.ref = ref;
			this.keys = keys;
		}

		public DBObject ref;
		public DBObject keys;
		public DBObject sortBy = null;
		public Integer limit = null;
	}

	public static class Count extends MongoAction {
		public Count(String dbName, String collectionName, DBObject ref) {
			super(dbName, collectionName);
			this.ref = ref;
		}

		public Count(String dbName, String collectionName) {
			this(dbName, collectionName, null);
		}

		public DBObject ref;
		public int options;
	}

	public static class Insert extends MongoAction {
		DBObject[] objs;

		public Insert(String dbName, String collectionName, DBObject[] objs) {
			super(dbName, collectionName);
			this.objs = objs;
		}

		public Insert(String dbName, String collectionName,
				DBObject object) {
			this(dbName, collectionName, new DBObject[]{object});
		}
	}

	public static class EnsureIndex extends MongoAction {
		public DBObject keys;
		public boolean unique;

		public EnsureIndex(String dbName, String collectionName, DBObject keys, boolean unique) {
			super(dbName, collectionName);
			this.keys = keys;
			this.unique = unique;
		}

		public EnsureIndex(String dbName, String collectionName, DBObject keys) {
			this(dbName, collectionName, keys, false);
		}
	}

	public static class Remove extends MongoAction {
		public DBObject keys;

		public Remove(String dbName, String collectionName, DBObject keys) {
			super(dbName, collectionName);
			this.keys = keys;
		}
	}
}
