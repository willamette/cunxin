package org.cunxin.reward.app.service

import com.google.inject.Inject
import org.cunxin.reward.app.dao.{PointsDao, BadgerDao}
import org.cunxin.reward.app.model.{Points, Badger}

class RewardService @Inject()(badgerDao: BadgerDao, pointsDao: PointsDao) {

    def getAllBadgers = badgerDao.findAll().map(_.data)

    def getAllPoints = pointsDao.findAll().map(_.data)

    def createBadger(badger: Badger) {
        badgerDao.create(badger)
    }

    def createPoints(points: Points) {
        pointsDao.create(points)
    }

}
