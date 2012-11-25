package org.cunxin.reward.app.model.reward.badger

import org.cunxin.reward.app.model.{UserActivity, UserAllTimeStats, UserEventType}
import org.cunxin.reward.app.model.reward.Badger

class DaAiWuJiangBadger extends Badger {
  def id = "daaiwujiangBadger"

  def observingEvents = List(UserEventType.DONATION)

  def onPublish(userId: String, projectId: String, eventType: UserEventType, data: Map[String, List[String]], pastAllTimeStats: UserAllTimeStats, pastActivities: List[UserActivity]): Int = {
    val totalTimes = pastActivities.filter(_.event == UserEventType.DONATION)
    val totalAmount = totalTimes.map(_.data("amount").head.toInt).sum
    if (totalTimes.size >= 49 && totalAmount + data("amount").head.toInt >= 5000) 1 else 0
  }
}
