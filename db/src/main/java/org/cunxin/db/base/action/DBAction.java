package org.cunxin.db.base.action;

public abstract class DBAction<Q, R> {
	public static enum Type {
		UPDATE, QUERY
	}

	public DBAction(Type type, String query, QueryGenerator<Q> queryGenerator, Executable<R> executable) {
		_type = type;
		_queryString = query;
		_queryGenerator = queryGenerator;
		_executable = executable;
	}

	public String getQueryString() {
		return _queryString;
	}

	public QueryGenerator<Q> getQueryGenerator() {
		return _queryGenerator;
	}

	public Type getType() {
		return _type;
	}

	public Executable<R> getExecutable() {
		return _executable;
	}

	private Executable<R> _executable;
	private QueryGenerator<Q> _queryGenerator;
	private String _queryString;
	private Type _type;
}
