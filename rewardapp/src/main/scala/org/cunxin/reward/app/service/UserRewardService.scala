package org.cunxin.reward.app.service

import com.google.inject.Inject
import org.cunxin.reward.app.dao.{BadgerDao, UserDao}
import org.cunxin.reward.app.model.Badger

class UserRewardService @Inject()(userDao: UserDao, badgerDao: BadgerDao) {

  def getBadger(userId: String, badgerId: String): Option[Badger] = {
    userDao.findUserById(userId) match {
      case None => None
      case Some(ud) => {
        if (ud.data.receivedBadgerIds.contains(badgerId))
          return badgerDao.findBadgerById(badgerId).map(_.data)
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

}
