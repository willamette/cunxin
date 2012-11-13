package org.cunxin.reward.app.dao

import java.util.Date
import com.google.inject.Inject
import com.mongodb.casbah.MongoDB
import org.cunxin.reward.app.model.UserActivity
import org.cunxin.support.db.{DBManagedModel, MongoRWDao, AbstractMongoDao}

class UserActivityDao @Inject()(db: MongoDB) extends AbstractMongoDao[UserActivity](db, "useractivity", version = 1) with MongoRWDao[UserActivity] {
    ensureIndex("userId" -> 1, "idx_userId", unique = false)
    ensureIndex("date" -> 1, "idx_date", unique = false)
    ensureIndex("projectId" -> 1, "idx_projectId", unique = false)

    //both are inclusive
    def findUserActivitiesByPeriod(userId: String, startDate: Date, endDate: Date): List[DBManagedModel[UserActivity]] = {
        find("userId" -> userId).filter(
            uds => uds.data.date.compareTo(startDate) > -1 && uds.data.date.compareTo(endDate) < 1
        )
    }

    def findUserActivitiesByPeriod(userId: String, startDate: Date): List[DBManagedModel[UserActivity]] = findUserActivitiesByPeriod(userId, startDate, new Date())

    def findUserActivities(userId: String): List[DBManagedModel[UserActivity]] = findUserActivitiesByPeriod(userId, new Date(70, 0, 1), new Date())

    def findUserActivitiesByPeriod(userId: String, projectId: String, startDate: Date, endDate: Date): List[DBManagedModel[UserActivity]] = {
        find("userId" -> userId, "projectId" -> projectId).filter(
            uds => uds.data.date.compareTo(startDate) > -1 && uds.data.date.compareTo(endDate) < 1
        )
    }

    def findUserActivitiesByPeriod(userId: String, projectId: String, startDate: Date): List[DBManagedModel[UserActivity]] = findUserActivitiesByPeriod(userId, projectId, startDate, new Date())

    def findUserActivities(userId: String, projectId: String): List[DBManagedModel[UserActivity]] = findUserActivitiesByPeriod(userId, projectId, new Date(70, 0, 1), new Date())
}
