package org.cunxin.db.base.action;

public class DBQuery<Q, R> extends DBAction<Q, R> {
	public DBQuery(String query, Executable<R> executable) {
		this(query, null, executable);
	}

	public DBQuery(String query, QueryGenerator<Q> queryGenerator, Executable<R> executable) {
		super(Type.QUERY, query, queryGenerator, executable);
	}
}
