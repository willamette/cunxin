package com.hulu.db.base;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.hulu.db.base.action.DBAction;
import com.hulu.db.base.action.Executable;
import com.hulu.db.base.action.Executable2;
import com.hulu.db.base.action.QueryGenerator;

public abstract class AbstractDBAdapter<A, B, Q, R> implements IDBAdapter<B, Q, R> {
	public void executeUpdateQueryWithTranscation(List<DBAction<Q, R>> queries) {
		throw new UnsupportedOperationException();
	}

	public void executeUpdate(B query) {
		executeUpdate(query, null);
	}

	public void executeUpdate(B query, QueryGenerator<Q> queryGenerator) {
		try {
			A conn = getMasterConnection();
			InternalExecuteUpdate(conn, query, queryGenerator);
			endQuery(conn);
		} catch (Exception e) {
			e.printStackTrace();
		};
	}

	public void executeQuery(B query, Executable<R> executable) {
		executeQuery(query, null, executable);
	}

	public void executeQuery(B query, QueryGenerator<Q> queryGenerator, Executable<R> executable) {
		executeQuery(query, queryGenerator, executable, null);
	}

	public void executeQuery(B query, QueryGenerator<Q> queryGenerator, Executable<R> executable, Map<String,String> queryPara) {
		try {
			A conn = getSlaveConnection();
			InternalExecuteQuery(conn, query, queryGenerator, executable, queryPara);
			endQuery(conn);
		} catch (Exception e) {
			e.printStackTrace();
		};
	}

	public <T> Iterator<T> executeQuery(B query, Executable2<? extends T, R> executable) {
		return executeQuery(query, null, executable);
	}

	//when iter.hasNext() == false, connection will be closed automatically
	public <T> Iterator<T> executeQuery(B query, QueryGenerator<Q> queryGenerator, Executable2<? extends T, R> executable) {
		try {
			return InternalExecuteQuery(getSlaveConnection(), query, queryGenerator, executable);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	protected void endQuery(A conn) {}

	abstract protected void InternalExecuteUpdate(A conn, B query,
			QueryGenerator<Q> queryGenerator) throws Exception;

	abstract protected void InternalExecuteQuery(A conn, B query,
			QueryGenerator<Q> queryGenerator, Executable<R> executable)
					throws Exception;

	abstract protected void InternalExecuteQuery(A conn, B query,
			QueryGenerator<Q> queryGenerator, Executable<R> executable, Map<String,String> queryPara)
					throws Exception;

	abstract protected <T> Iterator<T> InternalExecuteQuery(A conn,
			B query, QueryGenerator<Q> queryGenerator,
			Executable2<? extends T, R> executable) throws Exception;

	abstract public A getMasterConnection() throws Exception;

	abstract public A getSlaveConnection() throws Exception;
}
