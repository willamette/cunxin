package org.cunxin.reward.app.model.reward.badger

import org.cunxin.reward.app.model.{UserActivity, UserAllTimeStats, UserEventType}
import org.cunxin.reward.app.model.reward.Badger

class ZhongZhiChengChengBadger extends Badger{
  def id = "zhongzhichengchengBadger"

  def observingEvents = List(UserEventType.DONATION_SHARE)

  def onPublish(userId: String, projectId: String, eventType: UserEventType, data: Map[String, List[String]], pastAllTimeStats: UserAllTimeStats, pastActivities: List[UserActivity]) = 0
}
