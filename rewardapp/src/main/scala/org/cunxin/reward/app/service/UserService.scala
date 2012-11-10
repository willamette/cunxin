package org.cunxin.reward.app.service

import com.google.inject.Inject
import org.cunxin.reward.app.dao.UserDao
import org.cunxin.reward.app.model.User

class UserService @Inject()(userDao: UserDao) {

  def getAllUsers = userDao.findAll().map(_.data)

  def createNewUser(userId: String) {
    userDao.create(User(userId, 0, Set()))
  }

}
