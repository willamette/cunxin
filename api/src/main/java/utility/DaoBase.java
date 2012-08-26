package utility;

import com.hulu.db.mongodb.MongoDBAdapter;

public class DaoBase {
	protected MongoDBAdapter _adapter;
	public DaoBase(MongoDBAdapter adapter) {
		_adapter = adapter;
	}
}
