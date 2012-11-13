package org.cunxin.reward.app.service

import java.util.Date
import scala.collection.mutable
import com.google.inject.Inject
import org.apache.commons.logging.LogFactory
import org.cunxin.reward.app.model._
import org.cunxin.reward.app.dao.{UserActivityDao, UserAllTimeDao}

class UserEventService @Inject()(userAllTimeDao: UserAllTimeDao,
                                 userActivityDao: UserActivityDao,
                                 userRewardService: UserRewardService) {

    val logger = LogFactory.getLog(this.getClass)

    def recordEvent(userId: String, projectId: String, eventType: UserEventType, params: Map[String, List[String]]): Map[String, String] = {
        val result = userRewardService.publishActivity(userId, projectId, eventType, params)
        saveActivity(userId, projectId, eventType, params)
        updateAllTimeStats(userId, projectId, eventType)
        result
    }

    //TODO: Code smell
    private[this] def updateAllTimeStats(userId: String, projectId: String, eventType: UserEventType) {
        synchronized {
            userAllTimeDao.findUserAllTimeStatsByUserId(userId) match {
                case None => {
                    logger.info("User %s first stat data".format(userId))
                    val newEventHashMap = new mutable.HashMap[UserEventType, Int]()
                    newEventHashMap.+=(eventType -> 1)
                    val newProjectStatsHashMap = new mutable.HashMap[String, EventStats]()
                    val newAllTimeEventStats = new mutable.HashMap[UserEventType, Int]()
                    val newUserAllTimeStat = UserAllTimeStats(userId,
                        newProjectStatsHashMap += (projectId -> EventStats(newEventHashMap)),
                        EventStats(newAllTimeEventStats += (eventType -> 1)),
                        new Date())
                    userAllTimeDao.create(newUserAllTimeStat)
                }
                case Some(stat) => {
                    val eventStats = stat.data.projectStats.getOrElse(projectId, EventStats(stats = new mutable.HashMap[UserEventType, Int]()))
                    val count = eventStats.stats.getOrElseUpdate(eventType, 0) + 1
                    eventStats.stats.put(eventType, count)
                    stat.data.projectStats.put(projectId, eventStats)
                    val eventCount = stat.data.allEventStats.stats.getOrElse(eventType, 0) + 1
                    stat.data.allEventStats.stats.put(eventType, eventCount)
                    userAllTimeDao.update(stat)
                }
            }
        }
    }

    private[this] def saveActivity(userId: String, projectId: String, eventType: UserEventType, data: Map[String, List[String]]) {
        val activity = UserActivity(userId, projectId, eventType, data, new Date())
        userActivityDao.create(activity)
    }
}
