package org.cunxin.reward.app.service

import com.google.inject.Inject
import org.cunxin.reward.app.dao.UserInfoDao
import org.cunxin.reward.app.model.UserInfo

class UserInfoService @Inject()(userDao: UserInfoDao) {

  def getAllUsers = userDao.findAll().map(_.data)

  def updateRewards(userId: String, rewardId: String, value: String) {
    userDao.findUserById(userId) match {
      case None => userDao.create(UserInfo(userId, value.toInt, Set()))
      case Some(uD) =>
        val newUd = if (rewardId.endsWith("Points")) {
          uD.copy(data = uD.data.copy(points = uD.data.points + value.toInt))
        } else if (rewardId.endsWith("Badger") && value == "1") {
          uD.copy(data = uD.data.copy(receivedBadgerIds = uD.data.receivedBadgerIds + rewardId))
        } else uD
        userDao.update(newUd)
    }
  }

  def getPoints(userId: String): Int = {
    userDao.findUserById(userId) match {
      case None => 0
      case Some(uD) => uD.data.points
    }
  }

}
