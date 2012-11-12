package org.cunxin.reward.app.service

import org.testng.annotations.Test
import org.cunxin.support.sandbox.EmbedDb
import uk.me.lings.scalaguice.ScalaModule
import org.cunxin.reward.app.dao.{UserDao, BadgerDao, UserBadgerDao}
import uk.me.lings.scalaguice.InjectorExtensions.ScalaInjector
import com.google.inject.Guice
import org.cunxin.reward.app.model.reward.badger.Badger
import org.cunxin.reward.app.model.{UserEventType, Badger}
import org.testng.Assert

@Test
class BadgerSystemTest extends EmbedDb {

  private[this] val module = new ScalaModule {
    def configure() {
      bind[UserBadgerDao].asEagerSingleton()
      bind[BadgerDao].asEagerSingleton()
      bind[UserDao].asEagerSingleton()

      bind[UserEventService].asEagerSingleton()
      bind[UserRewardService].asEagerSingleton()
      bind[UserService].asEagerSingleton()
      bind[RewardService].asEagerSingleton()
    }
  }

  def test() {
    val injector = new ScalaInjector(Guice.createInjector(module, mongoModule))

    val userBadgerDao = injector.instance[UserBadgerDao]
    val badgerDao = injector.instance[BadgerDao]
    val userDao = injector.instance[UserDao]

    val userService = injector.instance[UserService]
    val userEventService = injector.instance[UserEventService]
    val userRewardService = injector.instance[UserRewardService]
    val badgerService = injector.instance[RewardService]

    val badger = Badger("1", "FirstBlood", Map(UserEventType.DONATION -> 1, UserEventType.SUPPORT_CLICK -> 2))
    val userId = "1"

    userService.createNewUser(userId)
    Assert.assertTrue(userBadgerDao.findUserBadgersByUser(userId).isEmpty)

    badgerService.createBadger(badger)
    Assert.assertTrue(badgerDao.findBadgerById(badger.id).isDefined)
    Assert.assertEquals(userBadgerDao.findUserBadgersByUser(userId).size, 1)

    userEventService.recordEvent(userId, UserEventType.WEIBO_SHARE)
    userEventService.recordEvent(userId, UserEventType.DONATION)
    userEventService.recordEvent(userId, UserEventType.SUPPORT_CLICK)

    Assert.assertTrue(userDao.findUserById(userId).isDefined)
    Assert.assertTrue(userDao.findUserById(userId).get.data.receivedBadgerIds.isEmpty)
    userEventService.recordEvent(userId, UserEventType.SUPPORT_CLICK)
    Assert.assertTrue(userDao.findUserById(userId).isDefined)
    Assert.assertEquals(userDao.findUserById(userId).get.data.receivedBadgerIds, Set(badger.id))

    Assert.assertTrue(userRewardService.getBadger(userId, badger.id).isDefined)
    Assert.assertEquals(userRewardService.getAllBadger(userId), Set(badger))

  }

}
