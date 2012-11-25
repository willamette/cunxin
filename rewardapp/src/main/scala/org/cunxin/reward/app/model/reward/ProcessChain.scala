package org.cunxin.reward.app.model.reward

import collection.mutable
import org.apache.commons.logging.LogFactory
import org.cunxin.reward.app.model.UserEventType

object ProcessChain {
  private[this] val log = LogFactory.getLog(this.getClass)
  private[this] val badgerMap = mutable.HashMap[String, Badger]()
  private[this] val pointsMap = mutable.HashMap[String, Points]()

  private[this] val rewardSubscriptionMap = mutable.HashMap[UserEventType, mutable.HashSet[Reward]]()

  def registerBadger(badger: Badger) {
    log.info("Registering badger id: " + badger.id)
    badgerMap.put(badger.id, badger)
  }

  def registerPoints(points: Points) {
    log.info("Registering points id: " + points.id)
    pointsMap.put(points.id, points)
  }

  def subscribeEvents(reward: Reward, events: List[UserEventType]) {
    events.foreach(e => {
      val rewards = rewardSubscriptionMap.getOrElseUpdate(e, new mutable.HashSet[Reward]())
      rewards.add(reward)
    })
  }

  def getAllBadgers = badgerMap.values.toList

  def getAllPoints = pointsMap.values.toList

  def getAllRewards(eventType: UserEventType) = rewardSubscriptionMap.getOrElse(eventType, mutable.HashSet())
}
