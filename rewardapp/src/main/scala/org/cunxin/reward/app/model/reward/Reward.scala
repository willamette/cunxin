package org.cunxin.reward.app.model.reward

import com.google.inject.Inject
import org.cunxin.reward.app.model.{UserActivity, UserAllTimeStats, UserEventType}
import org.cunxin.reward.app.ProcessChain
import java.util.Date

trait Reward {
  def id: String

  def observingEvents: List[UserEventType]

  def register()

  def onPublish(userId: String,
                projectId: String,
                date: Date,
                eventType: UserEventType,
                data: Map[String, List[String]],
                pastAllTimeStats: UserAllTimeStats,
                pastActivities: List[UserActivity]): Int

  @Inject
  def subscribeEvents() {
    ProcessChain.subscribeEvents(this, observingEvents)
  }
}
