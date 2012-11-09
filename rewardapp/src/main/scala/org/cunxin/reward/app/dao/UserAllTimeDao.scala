package org.cunxin.reward.app.dao

import com.google.inject.Inject
import com.mongodb.casbah.MongoDB
import org.cunxin.support.db.{MongoRWDao, AbstractMongoDao}
import org.cunxin.reward.app.model.UserAllTimeStats

class UserAllTimeDao @Inject()(db: MongoDB)
        extends AbstractMongoDao[UserAllTimeStats](db, "useralltimestats", version = 1)
        with MongoRWDao[UserAllTimeStats] {
    ensureIndex("userId" -> 1, "idx_userId", unique = true)
    // 日期格式应该为年月日-0时0分0秒 这样方便建立索引
    ensureIndex("date" -> 1, "idx_date", unique = true)

}
