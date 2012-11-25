package org.cunxin.reward.app.model.reward.points

import org.cunxin.reward.app.model.{UserActivity, UserAllTimeStats, UserEventType}
import org.cunxin.reward.app.model.reward.Points

class DonationPoints extends Points {
  def id = "donationPoints"

  def observingEvents = List(UserEventType.DONATION)

  def onPublish(userId: String, projectId: String, eventType: UserEventType, data: Map[String, List[String]],
                pastAllTimeStats: UserAllTimeStats, pastActivities: List[UserActivity]): Int = {
    val basicPoints = {
      if (pastAllTimeStats.projectStats.contains(projectId) &&
        pastAllTimeStats.projectStats(projectId).stats.contains(UserEventType.DONATION))
        0
      else
        1
    }
    val bonusPoints = data.getOrElse("amount", List("0")).head.toInt
    basicPoints + bonusPoints
  }
}
