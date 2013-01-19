package org.cunxin.reward.app.service

import com.google.inject.Guice
import org.testng.annotations.Test
import uk.me.lings.scalaguice.ScalaModule
import uk.me.lings.scalaguice.InjectorExtensions.ScalaInjector
import org.cunxin.support.sandbox.EmbedDb
import org.cunxin.reward.app.dao.{UserInfoDao, UserAllTimeDao, UserActivityDao}
import org.cunxin.reward.app.model.UserEventType
import org.cunxin.support.util.{HttpDataStrategyImpl, HttpDataStrategy}
import org.apache.http.client.HttpClient
import org.apache.http.params.{CoreConnectionPNames, BasicHttpParams}
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager
import org.cunxin.reward.app.model.reward.badger._
import org.cunxin.reward.app.module.{PointsInstancesModule, BadgersInstancesModule}
import org.testng.Assert

@Test
class BadgerSystemTest extends EmbedDb {

//  private[this] val module = new ScalaModule {
//    def configure() {
//      install(new BadgersInstancesModule)
//      install(new PointsInstancesModule)
//
//      bind[HttpClient].toInstance(createHttpClient)
//      bind[HttpDataStrategyImpl].asEagerSingleton()
//      bind[HttpDataStrategy].to[HttpDataStrategyImpl]
//
//      bind[UserActivityDao].asEagerSingleton()
//      bind[UserAllTimeDao].asEagerSingleton()
//      bind[UserInfoDao].asEagerSingleton()
//
//      bind[UserEventService].asEagerSingleton()
//      bind[UserRewardService].asEagerSingleton()
//      bind[UserInfoService].asEagerSingleton()
//    }
//
//    private[this] def createHttpClient: HttpClient = {
//      val params = new BasicHttpParams
//      params.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60 * 1000) //1 minute
//      new DefaultHttpClient(createConnectionManager, params)
//    }
//
//    private[this] def createConnectionManager: ThreadSafeClientConnManager = {
//      val m = new ThreadSafeClientConnManager()
//      m.setDefaultMaxPerRoute(10)
//      m.setMaxTotal(10000)
//      m
//    }
//  }
//
//  def testShiYuZuXiaBadger() {
//    val injector = new ScalaInjector(Guice.createInjector(module, mongoModule))
//    val userEventService = injector.instance[UserEventService]
//    val userInfoService = injector.instance[UserInfoService]
//    val shiYuZuXiaBadger = injector.instance[ShiYuZuXiaBadger]
//
//    val userId = "userId1"
//    val projectId = "projectId1"
//
//    val result = userEventService.recordEvent(userId, projectId, UserEventType.DONATION, Map())
//    Assert.assertEquals(result(shiYuZuXiaBadger.id), "1")
//    Assert.assertTrue(userInfoService.getBadgerIds(userId).contains(shiYuZuXiaBadger.id))
//  }
//
//  def testYiJinMianBoBadger() {
//    val injector = new ScalaInjector(Guice.createInjector(module, mongoModule))
//    val userEventService = injector.instance[UserEventService]
//    val userInfoService = injector.instance[UserInfoService]
//    val yiJinMianBoBadger = injector.instance[YiJinMianBoBadger]
//
//    val userId = "userId2"
//    val projectId = "projectId2"
//
//    for (i <- 1 to 99) userEventService.recordEvent(userId, projectId, UserEventType.SUPPORT, Map())
//    val result = userEventService.recordEvent(userId, projectId, UserEventType.SUPPORT, Map())
//    Assert.assertEquals(result(yiJinMianBoBadger.id), "1")
//    Assert.assertTrue(userInfoService.getBadgerIds(userId).contains(yiJinMianBoBadger.id))
//  }
//
//  def testDaAiWuJiangBadger() {
//    val injector = new ScalaInjector(Guice.createInjector(module, mongoModule))
//    val userEventService = injector.instance[UserEventService]
//    val userInfoService = injector.instance[UserInfoService]
//    val daAiWuJiangBadger = injector.instance[DaAiWuJiangBadger]
//
//    val userId = "userId3"
//    val projectId = "projectId3"
//
//    for (i <- 1 to 49) userEventService.recordEvent(userId, projectId, UserEventType.DONATION, Map("amount" -> List("100")))
//    val result = userEventService.recordEvent(userId, projectId, UserEventType.DONATION, Map("amount" -> List("200")))
//    Assert.assertEquals(result(daAiWuJiangBadger.id), "1")
//    Assert.assertTrue(userInfoService.getBadgerIds(userId).contains(daAiWuJiangBadger.id))
//  }
}
