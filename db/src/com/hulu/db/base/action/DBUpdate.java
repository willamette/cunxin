package com.hulu.db.base.action;

import java.sql.ResultSet;

public class DBUpdate<Q> extends DBAction<Q, ResultSet> {
	public DBUpdate(String query) {
		this(query, null);
	}

	public DBUpdate(String query, QueryGenerator<Q> queryGenerator) {
		super(Type.UPDATE, query, queryGenerator, null);
	}
}
