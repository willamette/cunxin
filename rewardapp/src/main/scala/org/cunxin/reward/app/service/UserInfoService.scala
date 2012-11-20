package org.cunxin.reward.app.service

import com.google.inject.Inject
import org.cunxin.reward.app.dao.UserInfoDao
import org.cunxin.reward.app.model.UserInfo

class UserInfoService @Inject()(userDao: UserInfoDao) {

    def getAllUsers = userDao.findAll().map(_.data)

    def updatePoints(userId: String, points: Int) {
        userDao.findUserById(userId) match {
            case None => userDao.create(UserInfo(userId, points, Set()))
            case Some(uD) =>
                val newUD = uD.copy(data = uD.data.copy(points = uD.data.points + points))
                userDao.update(newUD)
        }
    }

    def getPoints(userId: String): Int = {
        userDao.findUserById(userId) match {
            case None => 0
            case Some(uD) => uD.data.points
        }
    }

}
