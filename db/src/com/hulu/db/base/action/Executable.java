package com.hulu.db.base.action;

public abstract class Executable<R> {
	public abstract void execute(R rs) throws Exception;
}
