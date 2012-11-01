package org.cunxin.reward.app.service

import org.testng.annotations.Test
import org.cunxin.support.sandbox.EmbedDb
import uk.me.lings.scalaguice.ScalaModule
import org.cunxin.reward.app.dao.{UserDao, BadgerDao, UserBadgerDao}
import uk.me.lings.scalaguice.InjectorExtensions.ScalaInjector
import com.google.inject.Guice
import org.cunxin.reward.app.model.{UserEventType, Badger}


@Test
class BadgerTest extends EmbedDb {

  private[this] val module = new ScalaModule {
    def configure() {

      bind[UserBadgerDao].asEagerSingleton()
      bind[BadgerDao].asEagerSingleton()
      bind[UserDao].asEagerSingleton()

      bind[UserEventService].asEagerSingleton()
      bind[UserRewardService].asEagerSingleton()
      bind[UserService].asEagerSingleton()
      bind[BadgerService].asEagerSingleton()
    }
  }

  def test() {
    val injector = new ScalaInjector(Guice.createInjector(module, mongoModule))
    val badger = Badger("1", "FirstBlood", Map(UserEventType.DONATION -> 1, UserEventType.SUPPORT_CLICK -> 2))
    val
  }

}
