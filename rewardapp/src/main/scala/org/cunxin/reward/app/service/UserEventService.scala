package org.cunxin.reward.app.service

import java.util.Date
import scala.collection.mutable
import com.google.inject.Inject
import org.apache.commons.logging.LogFactory
import org.cunxin.support.db.DBManagedModel
import org.cunxin.reward.app.model._
import org.cunxin.reward.app.dao.{UserDailyStatsDao, UserAllTimeDao, UserDao}

class UserEventService @Inject()(userDao: UserDao,
                                 userAllTimeDao: UserAllTimeDao,
                                 userDailyDao: UserDailyStatsDao) {

    val logger = LogFactory.getLog(this.getClass)

    def recordEvent(userId: String, projectId: String, eventType: UserEventType) {
        userDao.findUserById(userId) match {
            case None => logger.error("User %s doesn't exist".format(userId))
            case Some(user) => {
                val today = new Date()
                today.setHours(0)
                today.setSeconds(0)
                today.setMinutes(0)
                updateAllTimeStats(user, projectId, eventType)
                updateDailyStats(user, projectId, eventType, today)
            }
        }
    }

    private[this] def updateAllTimeStats(user: DBManagedModel[User], projectId: String, eventType: UserEventType) {
        synchronized {
            userAllTimeDao.findUserAllTimeStatsByUserId(user.data.id) match {
                case None => {
                    logger.info("User %s first stat data".format(user.data.id))
                    val newEventHashMap = new mutable.HashMap[UserEventType, Int]()
                    newEventHashMap.+=(eventType -> 1)
                    val newProjectStatsHashMap = new mutable.HashMap[String, EventStats]()
                    val newUserAllTimeStat = UserAllTimeStats(user.data.id, newProjectStatsHashMap += (projectId -> EventStats(newEventHashMap)), new Date())
                    userAllTimeDao.create(newUserAllTimeStat)
                }
                case Some(stat) => {
                    val eventStats = stat.data.projectStats.getOrElse(projectId, EventStats(stats = new mutable.HashMap[UserEventType, Int]()))
                    val count = eventStats.stats.getOrElseUpdate(eventType, 0) + 1
                    eventStats.stats.put(eventType, count)
                    stat.data.projectStats.put(projectId, eventStats)
                    userAllTimeDao.update(stat)
                }
            }
        }
    }

    private[this] def updateDailyStats(user: DBManagedModel[User], projectId: String, eventType: UserEventType, today: Date) {
        synchronized {
            userDailyDao.findUserStatsByDate(user.data.id, today) match {
                case None => {
                    logger.info("User %s first stat data for date %s".format(user.data.id, today.toString))
                    val newEventHashMap = new mutable.HashMap[UserEventType, Int]()
                    newEventHashMap.+=(eventType -> 1)
                    val newProjectStatsHashMap = new mutable.HashMap[String, EventStats]()
                    val userDailyStats = UserDailyStats(user.data.id, newProjectStatsHashMap += (projectId -> EventStats(newEventHashMap)), new Date())
                    userDailyDao.create(userDailyStats)
                }
                case Some(stat) => {
                    val eventStats = stat.data.projectStats.getOrElse(projectId, EventStats(stats = new mutable.HashMap[UserEventType, Int]()))
                    val count = eventStats.stats.getOrElseUpdate(eventType, 0) + 1
                    eventStats.stats.put(eventType, count)
                    stat.data.projectStats.put(projectId, eventStats)
                    userDailyDao.update(stat)
                }
            }
        }
    }
}
