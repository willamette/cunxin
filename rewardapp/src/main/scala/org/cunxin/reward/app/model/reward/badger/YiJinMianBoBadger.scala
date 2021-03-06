package org.cunxin.reward.app.model.reward.badger

import org.cunxin.reward.app.model.{UserActivity, UserAllTimeStats, UserEventType}
import org.cunxin.reward.app.model.reward.Badger
import java.util.Date

class YiJinMianBoBadger extends Badger {
  def id = "yijinmianboBadger"

  def observingEvents = List(UserEventType.SUPPORT)

  def onPublish(userId: String, projectId: String,date: Date, eventType: UserEventType, data: Map[String, List[String]], pastAllTimeStats: UserAllTimeStats, pastActivities: List[UserActivity]): Int = {
    pastAllTimeStats.allEventStats.stats.get(UserEventType.SUPPORT) match {
      case None => 0
      case Some(times) => if (times == 99) 1 else 0
    }
  }
}
