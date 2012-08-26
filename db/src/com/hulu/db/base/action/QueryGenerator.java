package com.hulu.db.base.action;

public abstract class QueryGenerator<Q> {
	public abstract void execute(Q s)  throws Exception;
}
