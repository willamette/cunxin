package org.cunxin.reward.app.model.reward.points

import org.cunxin.reward.app.model.{UserActivity, UserAllTimeStats, UserEventType}
import org.cunxin.reward.app.model.reward.Points
import java.util.Date

class DonationPoints extends Points {
  def id = "donationPoints"

  def observingEvents = List(UserEventType.DONATION)

  def onPublish(userId: String, projectId: String, date: Date, eventType: UserEventType, data: Map[String, List[String]],
                pastAllTimeStats: UserAllTimeStats, pastActivities: List[UserActivity]): Int = {
    val basicPoints = {
      if (pastAllTimeStats.projectStats.contains(projectId) &&
        pastAllTimeStats.projectStats(projectId).stats.contains(UserEventType.DONATION))
        0
      else
        3
    }
    val bonusPoints = data.getOrElse("amount", List("0")).head.toInt
    basicPoints + bonusPoints
  }
}
