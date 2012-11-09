package org.cunxin.reward.app.dao

import com.google.inject.Inject
import com.mongodb.casbah.MongoDB
import org.cunxin.support.db.{MongoRWDao, AbstractMongoDao}
import org.cunxin.reward.app.model.UserDailyStats
import java.util.Date

class UserDailyStatsDao @Inject()(db: MongoDB) extends AbstractMongoDao[UserDailyStats](db, "userdailystats", version = 1) with MongoRWDao[UserDailyStats] {
    ensureIndex("userId" -> 1, "idx_userId", unique = true)
    // 日期格式应该为年月日-0时0分0秒 这样方便建立索引
    ensureIndex("date" -> 1, "idx_date", unique = true)

    //both are inclusive
    def findUserStatsByDate(userId: String, startDate: Date, endDate: Date): List[UserDailyStats] = {
        find("userId" -> userId).map(_.data).filter(uds => uds.date.before(startDate) && uds.date.after(endDate))
    }

}
