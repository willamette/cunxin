package org.cunxin.api.utility;

import org.cunxin.db.mongodb.MongoDBAdapter;

public class DaoBase {
	protected MongoDBAdapter _adapter;
	public DaoBase(MongoDBAdapter adapter) {
		_adapter = adapter;
	}
}
