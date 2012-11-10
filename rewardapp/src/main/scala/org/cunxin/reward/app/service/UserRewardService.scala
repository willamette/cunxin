package org.cunxin.reward.app.service

import com.google.inject.Inject
import org.cunxin.reward.app.dao.{UserDailyStatsDao, UserAllTimeDao, BadgerDao, UserDao}
import org.cunxin.reward.app.model.{User, Badger}
import org.cunxin.support.db.DBManagedModel

class UserRewardService @Inject()(userDao: UserDao,
                                  badgerDao: BadgerDao,
                                  userAllTimeDao: UserAllTimeDao,
                                  userDailyDao: UserDailyStatsDao) {

    def getBadger(userId: String, badgerId: String): Option[Badger] = {
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

    def getAllBadger(userId: String): Set[Badger] = {
        userDao.findUserById(userId) match {
            case None => Set()
            case Some(ud) => ud.data.receivedBadgerIds.map(badgerId => badgerDao.findBadgerById(badgerId).map(_.data)).flatten
        }
    }

    def getPoints(userId: String, projectId: String): Int = {

    }

    def isQualifiedForBadger(userDataModel: DBManagedModel[User], badger: Badger): Boolean = {
        userAllTimeDao.findUserAllTimeStatsByUserId(userDataModel.data.id) match {
            case None => false
            case Some(uald) => badger.qualifyRule(uald.data.projectStats)
        }
    }

}
