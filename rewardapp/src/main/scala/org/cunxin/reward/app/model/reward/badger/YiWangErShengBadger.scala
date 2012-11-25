package org.cunxin.reward.app.model.reward.badger

import org.cunxin.reward.app.model.{UserActivity, UserAllTimeStats, UserEventType}
import org.cunxin.reward.app.model.reward.Badger

class YiWangErShengBadger extends Badger {
  def id = "yiwangershengBadger"

  def observingEvents = List(UserEventType.DONATION_SHARE)

  def onPublish(userId: String, projectId: String, eventType: UserEventType, data: Map[String, List[String]], pastAllTimeStats: UserAllTimeStats, pastActivities: List[UserActivity]): Int = {
    pastAllTimeStats.allEventStats.stats.get(UserEventType.DONATION_SHARE) match {
      case None => 1
      case Some(times) => 0
    }
  }
}
