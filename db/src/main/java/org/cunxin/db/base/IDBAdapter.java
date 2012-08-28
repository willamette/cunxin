package org.cunxin.db.base;

import java.util.Iterator;
import java.util.List;

import org.cunxin.db.base.action.DBAction;
import org.cunxin.db.base.action.Executable;
import org.cunxin.db.base.action.Executable2;
import org.cunxin.db.base.action.QueryGenerator;

public interface IDBAdapter<B, Q, R> {

	void executeUpdateQueryWithTranscation(List<DBAction<Q, R>> queries);

	void executeUpdate(B query);

	void executeUpdate(B query, QueryGenerator<Q> queryGenerator);

	void executeQuery(B query, Executable<R> executable);

	void executeQuery(B query, QueryGenerator<Q> queryGenerator,
			Executable<R> executable);

	<T> Iterator<T> executeQuery(B query,
			Executable2<? extends T, R> executable);

	<T> Iterator<T> executeQuery(B query, QueryGenerator<Q> queryGenerator,
			Executable2<? extends T, R> executable);
}
