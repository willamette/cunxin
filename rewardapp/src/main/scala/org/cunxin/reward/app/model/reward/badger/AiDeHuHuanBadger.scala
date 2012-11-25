package org.cunxin.reward.app.model.reward.badger

import org.cunxin.reward.app.model.{UserActivity, UserAllTimeStats, UserEventType}
import org.cunxin.reward.app.model.reward.Badger

class AiDeHuHuanBadger extends Badger {
  def id = "aidehuhuanBadger"

  def observingEvents = List(UserEventType.WEIBO_SHARE)

  def onPublish(userId: String, projectId: String, eventType: UserEventType, data: Map[String, List[String]], pastAllTimeStats: UserAllTimeStats, pastActivities: List[UserActivity]): Int = {
    pastAllTimeStats.allEventStats.stats.get(UserEventType.WEIBO_SHARE) match {
      case None => 0
      case Some(times) => if (times == 99) 1 else 0
    }
  }
}
