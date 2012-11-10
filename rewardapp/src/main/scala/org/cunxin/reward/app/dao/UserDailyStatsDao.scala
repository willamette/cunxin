package org.cunxin.reward.app.dao

import java.util.Date
import com.google.inject.Inject
import com.mongodb.casbah.MongoDB
import org.cunxin.reward.app.model.UserDailyStats
import org.cunxin.support.db.{MongoRWDao, AbstractMongoDao}

class UserDailyStatsDao @Inject()(db: MongoDB) extends AbstractMongoDao[UserDailyStats](db, "userdailystats", version = 1) with MongoRWDao[UserDailyStats] {
    ensureIndex("userId" -> 1, "idx_userId", unique = true)
    // 日期格式应该为年月日-0时0分0秒 这样方便建立索引
    ensureIndex("date" -> 1, "idx_date", unique = true)

    //both are inclusive
    def findUserStatsByPeriod(userId: String, startDate: Date, endDate: Date) = {
        find("userId" -> userId).filter(
            uds => uds.data.date.compareTo(startDate) > -1 && uds.data.date.compareTo(startDate) < 1
        )
    }

    def findUserStatsByPeriod(userId: String, startDate: Date) = findUserStatsByPeriod(userId, startDate, new Date())

    def findUserStatsByDate(userId: String, date: Date) = find("userId" -> userId, "date" -> date).headOption
}
