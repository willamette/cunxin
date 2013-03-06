package org.cunxin.reward.app.model.reward.points

import java.util.Date
import org.cunxin.reward.app.model.{UserActivity, UserAllTimeStats, UserEventType}
import org.cunxin.reward.app.model.reward.Points
import org.apache.commons.logging.LogFactory

class SupportPoints extends Points {

  private [this] val logger = LogFactory.getLog(this.getClass)

  def id = "supportPoints"

  def observingEvents = List(UserEventType.SUPPORT)

  def onPublish(userId: String, projectId: String, date: Date, eventType: UserEventType, data: Map[String, List[String]], pastAllTimeStats: UserAllTimeStats, pastActivities: List[UserActivity]): Int = {
    pastAllTimeStats.projectStats.get(projectId) match {
      case None =>
      case Some(m) => if (m.stats.contains(UserEventType.SUPPORT)) {
        logger.info("User %s has supported project %s before".format(userId, projectId))
        return 0
      }
    }
    val today = new Date(date.getYear, date.getMonth, date.getDate)
    val todayActivities = pastActivities.filter(a => a.date.compareTo(today) > 0 && a.event == UserEventType.SUPPORT)
    logger.info("User %s has supported %d projects in day %s".format(userId, todayActivities.size, today.toString))
    if (todayActivities.size >= 5) 0 else 1
  }
}
