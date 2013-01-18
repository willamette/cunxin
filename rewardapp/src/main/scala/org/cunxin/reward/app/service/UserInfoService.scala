package org.cunxin.reward.app.service

import com.google.inject.Inject
import org.cunxin.reward.app.dao.UserInfoDao
import org.cunxin.reward.app.model.UserInfo
import java.util.Date

class UserInfoService @Inject()(userDao: UserInfoDao) {

  def getAllUsers = userDao.findAll().map(_.data)

  def updateRewards(userId: String, rewardId: String, value: String) {
    userDao.findUserById(userId) match {
      case None => userDao.create(UserInfo(userId, value.toInt, new Date(), 1, Set()))
      case Some(uD) =>
        val newUd = if (rewardId.endsWith("Points")) {
          uD.copy(data = uD.data.copy(points = uD.data.points + value.toInt))
        } else if (rewardId.endsWith("Badger") && value == "1") {
          uD.copy(data = uD.data.copy(receivedBadgerIds = uD.data.receivedBadgerIds + rewardId))
        } else uD
        userDao.update(newUd)
    }
  }

  def updateAndGetLoginDays(userId: String, loginDate: Date): Int = {
    userDao.findUserById(userId) match {
      case None => {
        userDao.create(UserInfo(userId, 0, loginDate, 1, Set()))
        1
      }
      case Some(uD) =>
        val newUd = {
          val lastLoginTime = uD.data.lastLoginDate.getTime
          val loginDays = if (loginDate.getTime - lastLoginTime < 1000 * 60 * 60 * 24) uD.data.loginDays + 1 else 1
          uD.copy(data = uD.data.copy(lastLoginDate = loginDate, loginDays = loginDays))
        }
        userDao.update(newUd)
        newUd.data.loginDays
    }
  }

  def getPoints(userId: String): Int = {
    userDao.findUserById(userId) match {
      case None => 0
      case Some(uD) => uD.data.points
    }
  }

  def getBadgerIds(userId: String): Set[String] = {
    userDao.findUserById(userId) match {
      case None => Set()
      case Some(uD) => uD.data.receivedBadgerIds
    }
  }

}
