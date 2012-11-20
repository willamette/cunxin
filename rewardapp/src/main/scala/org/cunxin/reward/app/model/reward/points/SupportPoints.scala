package org.cunxin.reward.app.model.reward.points

import java.util.Date
import org.cunxin.reward.app.model.{UserActivity, UserAllTimeStats, UserEventType}

class SupportPoints extends Points {
  def id = "supportPoints"

  def observingEvents = List(UserEventType.SUPPORT)

  def onPublish(userId: String, projectId: String, eventType: UserEventType, data: Map[String, List[String]], pastAllTimeStats: UserAllTimeStats, pastActivities: List[UserActivity]): Int = {
    val today = new Date()
    today.setHours(0)
    today.setMinutes(0)
    today.setSeconds(0)
    val todayActivities = pastActivities.filter(a => a.date.compareTo(today) > -1 && a.event == UserEventType.SUPPORT)
    if (todayActivities.size >= 5) 0 else 1
  }
}
