package org.cunxin.db.base.action;

public abstract class Executable2<T, R> extends Executable<R> {
	private T result;
	public void execute(R rs) throws Exception {
		result = generateFromResultSet(rs);
	}

	public T getResult() {
		return result;
	}

	protected abstract T generateFromResultSet(R rs) throws Exception;
}
