package utility;

import java.util.Iterator;

import org.cunxin.app.model.JsonConverter;
import org.cunxin.app.model.User;

import com.hulu.db.base.action.Executable2;
import com.hulu.db.mongodb.MongoAction.Find;
import com.hulu.db.mongodb.MongoAction.Insert;
import com.hulu.db.mongodb.MongoAction.Remove;
import com.hulu.db.mongodb.MongoAction.Update;
import com.hulu.db.mongodb.MongoDBAdapter;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class DaoUser extends DaoBase{
	public DaoUser(MongoDBAdapter adapter) {
		super(adapter);
	}

	String dbName = "cx";
	String collectionName = "user";
	
	public boolean addUser(User user) {
		User formerUser = findUser(JsonConverter.toJson(user));
		if (formerUser != null)
			return false;
		addUserDirectly(user);
		return true;
	}
	
	private void addUserDirectly(User user) {
		DBObject obj = new BasicDBObject();
		Insert insert = new Insert(dbName, collectionName, obj);
		_adapter.executeUpdate(insert);
	}
	
	public void updateUser(User user) {
		Update update = new Update(dbName, collectionName, new BasicDBObject("mail", user.mail),  (DBObject)JSON.parse(JsonConverter.toJson(user)));
		_adapter.executeUpdate(update);
	}
	
	public void deleteUser(String mail) {
		Remove remove = new Remove(dbName, collectionName, new BasicDBObject("mail", mail));
		_adapter.executeUpdate(remove);
	}
	
	public User findUser(String mail) {
		Find find = new Find(dbName, collectionName, new BasicDBObject("mail", mail));
		Iterator<User> iter = _adapter.executeQuery(find, new Executable2<User, DBObject>() {
			User tmp = new User();
			@Override
			protected User generateFromResultSet(DBObject rs) throws Exception {
				return JsonConverter.fromJson(tmp, rs.toString());
			}
		});
		if(iter.hasNext())
			return iter.next();
		return null;
		
	}
}
