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

  def updateAndGetLoginDaysAndLastLoginDate(userId: String, loginDate: Date): (Date, Int) = {
    userDao.findUserById(userId) match {
      case None => {
        userDao.create(UserInfo(userId, 0, loginDate, 1, Set()))
        (new Date(0), 1)
      }
      case Some(uD) =>
        val lastLoginDate = uD.data.lastLoginDate
        val todayDate = new Date(loginDate.getYear, loginDate.getMonth, loginDate.getDate)
        val newUd = {
          val loginDays =
            if (todayDate.getTime > lastLoginDate.getTime) {  /* login in diff days */
              if (todayDate.getTime - lastLoginDate.getTime < 1000 * 60 * 60 * 24) uD.data.loginDays + 1 else 1
            } else uD.data.loginDays /* login in the same day */
          uD.copy(data = uD.data.copy(lastLoginDate = loginDate, loginDays = loginDays))
        }
        userDao.update(newUd)
        (lastLoginDate, newUd.data.loginDays)
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
