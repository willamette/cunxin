package org.cunxin.reward.app.model.reward

import badger.Badger
import points.Points
import collection.mutable
import org.apache.commons.logging.LogFactory

object RewardsIterator {
    private[this] val log = LogFactory.getLog(this.getClass)
    private[this] val badgerMap = mutable.HashMap[String, Badger]()
    private[this] val pointsMap = mutable.HashMap[String, Points]()

    def registerBadger(badger: Badger) {
        log.info("Registering badger id: " + badger.id)
        badgerMap.put(badger.id, badger)
    }

    def registerPoints(points: Points) {
        log.info("Registering points id: " + points.id)
        pointsMap.put(points.id, points)
    }

    def getAllBadgers = badgerMap.values.toList
    def getAllPoints = pointsMap.values.toList
}
