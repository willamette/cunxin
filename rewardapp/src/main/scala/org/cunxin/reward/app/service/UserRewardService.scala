package org.cunxin.reward.app.service

import com.google.inject.Inject
import org.cunxin.reward.app.dao._
import org.cunxin.support.db.DBManagedModel
import org.cunxin.reward.app.model.reward.badger.Badger
import org.cunxin.reward.app.model.reward.points.Points
import org.cunxin.reward.app.model.{Points, Badger, User}
import scala.Some
import java.util.Date

class UserRewardService @Inject()(userDao: UserDao,
                                  userAllTimeDao: UserAllTimeDao,
                                  userDailyDao: UserDailyStatsDao) {

    def checkBadgerStatus(userId: String, badgerId: String): Option[Badger] = {
        userDao.findUserById(userId) match {
            case None => None
            case Some(ud) => {
                val badger = badgerDao.findBadgerById(badgerId).map(_.data)
                if (ud.data.receivedBadgerIds.contains(badgerId))
                    return badger
                isQualifiedForBadger(ud, badger.get)
            }
            None
        }
    }

    def checkAllBadgerStatuses(userId: String): List[Badger] = {
        badgerDao.findAll().flatMap(dbM => checkBadgerStatus(userId, dbM.data.id))
    }

    def getAllReceivedBadger(userId: String): Set[Badger] = {
        userDao.findUserById(userId) match {
            case None => Set()
            case Some(ud) => ud.data.receivedBadgerIds.map(badgerId => badgerDao.findBadgerById(badgerId).map(_.data)).flatten
        }
    }

    def getProjectPoints(userId: String): Int = {
        userDao.findUserById(userId) match {
            case None => -1
            case Some(ud) => {
                val newQualifiedPoints = pointsDao.findAll().
                        filterNot(p => ud.data.receivedPointsIds.contains(p.data.id)).
                        flatMap(p => calculateQualifiedPoints(userId, p))
            }
        }
    }

    private[this] def calculateQualifiedPoints(userId: String, points: DBManagedModel[Points]): Option[Points] = {
        if (points.data.days == -1)
            userAllTimeDao.findUserAllTimeStatsByUserId(userId) match {
                case None => None
                case Some(allTimeStat) => if (points.data.qualifyRule(allTimeStat.data.projectStats)) Some(points.data) else None
            }
        else {
            //Only calculate the latest 1 weeks. full logs scan is so expensive
            val today = new Date()
            today.setHours(0)
            today.setMinutes(0)
            today.setSeconds(0)
            val startDate = new Date(today.getTime - 7 * 24 * 60 * 60 * 1000)
            val stats = userDailyDao.findUserStatsByDate(userId, startDate)
            stats.sliding()
        }
    }

    private[this] def isQualifiedForBadger(userDataModel: DBManagedModel[User], badger: Badger): Boolean = {
        userAllTimeDao.findUserAllTimeStatsByUserId(userDataModel.data.id) match {
            case None => false
            case Some(uald) => {
                val qualified = badger.qualifyRule(uald.data.projectStats)
                if (qualified)
                    userDao.update(userDataModel.copy(data = userDataModel.data.copy(receivedBadgerIds = userDataModel.data.receivedBadgerIds + badger.id)))
                qualified
            }
        }
    }

}
