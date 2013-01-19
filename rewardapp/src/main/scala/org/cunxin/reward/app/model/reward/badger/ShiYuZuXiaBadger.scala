package org.cunxin.reward.app.model.reward.badger

import org.cunxin.reward.app.model.{UserActivity, UserAllTimeStats, UserEventType}
import org.cunxin.reward.app.model.reward.Badger
import java.util.Date

class ShiYuZuXiaBadger extends Badger {
  def id = "shiyuzuxiaBadger"

  def observingEvents = List(UserEventType.DONATION)

  def onPublish(userId: String, projectId: String, date: Date, eventType: UserEventType, data: Map[String, List[String]], pastAllTimeStats: UserAllTimeStats, pastActivities: List[UserActivity]): Int = {
    pastAllTimeStats.allEventStats.stats.get(UserEventType.DONATION) match {
      case None => 1
      case Some(times) => 0
    }
  }
}
