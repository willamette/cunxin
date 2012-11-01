package org.cunxin.reward.app.service

import com.google.inject.Inject
import org.cunxin.reward.app.dao.{UserBadgerDao, BadgerDao, UserDao}
import org.cunxin.reward.app.model.{UserEventType, UserBadger, Badger}
import java.util.Date
import collection.mutable

class BadgerService @Inject()(userDao: UserDao, badgerDao: BadgerDao, userBadgerDao: UserBadgerDao) {

  def getAllBadgers = badgerDao.findAll().map(_.data)

  def createBadger(badger: Badger) {
    synchronized {
      badgerDao.create(badger)
      val statsMap = new mutable.HashMap[UserEventType, Int]()
      badger.rule.foreach(kv => statsMap.put(kv._1, 0))
      val userBadgersNewEntries = userDao.findAll().map(d => UserBadger(d.data.id, badger.id, statsMap, badger.rule, new Date()))
      userBadgersNewEntries.foreach(ub => userBadgerDao.create(ub))
    }
  }

}
