package org.cunxin.reward.app.service

import java.util.Date
import collection.mutable
import com.google.inject.Inject
import org.apache.commons.logging.LogFactory
import org.cunxin.reward.app.dao._
import org.cunxin.reward.app.model.{EventStats, UserAllTimeStats, UserEventType}
import org.cunxin.reward.app.ProcessChain

class UserRewardService @Inject()(userAllTimeDao: UserAllTimeDao,
                                  userActivityDao: UserActivityDao) {

  private[this] val logger = LogFactory.getLog(this.getClass)

  def publishActivity(userId: String,
                      projectId: String,
                      date: Date,
                      userEvent: UserEventType,
                      data: Map[String, List[String]]): Map[String, String] = {
    val pastAllTimeActivities = userAllTimeDao.findUserAllTimeStatsByUserId(userId).map(_.data).getOrElse(
      UserAllTimeStats(userId, new mutable.HashMap[String, EventStats], EventStats(new mutable.HashMap[UserEventType, Int]()), new Date())
    )
    logger.info("See event %s for user %s and project %s, data %s".format(userEvent.toString, userId, projectId, data.mkString(",")))
    val pastUserActivities = userActivityDao.findUserActivities(userId).map(_.data)
    ProcessChain.getAllRewards(userEvent).map {
      r => r.id -> r.onPublish(userId, projectId, date, userEvent, data, pastAllTimeActivities, pastUserActivities).toString
    }.toMap
  }

}
