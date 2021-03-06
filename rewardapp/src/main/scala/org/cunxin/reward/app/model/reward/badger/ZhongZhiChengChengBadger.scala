package org.cunxin.reward.app.model.reward.badger

import org.cunxin.reward.app.model.{UserActivity, UserAllTimeStats, UserEventType}
import org.cunxin.reward.app.model.reward.Badger
import java.util.Date

object ZhongZhiChengChengBadger extends Badger {
  def id = "zhongzhichengchengBadger"

  def observingEvents = List()

  def onPublish(userId: String, projectId: String, date: Date, eventType: UserEventType, data: Map[String, List[String]], pastAllTimeStats: UserAllTimeStats, pastActivities: List[UserActivity]) = 0
}
