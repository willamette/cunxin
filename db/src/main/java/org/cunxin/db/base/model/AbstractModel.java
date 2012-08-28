package org.cunxin.db.base.model;

import java.util.Collection;

public class AbstractModel implements IModel {
	protected int _id;

	public int getId() {
		return _id;
	}

	public void setId(int id) {
		_id = id;
	}

	protected String generateStringFromCollection(Collection<?> source) {
		StringBuilder buff = new StringBuilder();
		for (Object value : source) {
			buff.append(value).append(" ");
		}
		return buff.toString().trim();
	}
}
