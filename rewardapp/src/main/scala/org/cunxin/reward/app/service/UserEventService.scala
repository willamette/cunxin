package org.cunxin.reward.app.service

import java.util.Date
import com.google.inject.Inject
import org.cunxin.reward.app.dao.{UserDao, UserBadgerDao}
import org.cunxin.reward.app.model.{User, UserEventType}
import org.cunxin.support.db.DBManagedModel

class UserEventService @Inject()(userDao: UserDao, userBadgerDao: UserBadgerDao) {

  def recordEvent(userId: String, eventType: UserEventType) {
    userDao.findUserById(userId) match {
      case None =>
      case Some(user) => {
        updateBadger(user, eventType)
      }
    }
  }

  private[this] def updateBadger(user: DBManagedModel[User], eventType: UserEventType) {
    userBadgerDao.findUserBadgersByUser(user.data.id).foreach(
      ubd => ubd.data.stats.get(eventType) match {
        case None =>
        case Some(counter) =>
          ubd.data.stats.put(eventType, counter + 1)
          userBadgerDao.update(ubd.copy(ubd.data.copy(lastUpdateTime = new Date())))
          if (ubd.data.rule.equals(ubd.data.stats)) {
            userDao.update(user.copy(user.data.copy(receivedBadgerIds = user.data.receivedBadgerIds + ubd.data.badgerId)))
          }
      })
  }
}
