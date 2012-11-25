package org.cunxin.reward.app.service

import org.testng.Assert
import com.google.inject.Guice
import org.testng.annotations.Test
import uk.me.lings.scalaguice.ScalaModule
import uk.me.lings.scalaguice.InjectorExtensions.ScalaInjector
import org.cunxin.support.sandbox.EmbedDb
import org.cunxin.reward.app.dao.{UserInfoDao, UserAllTimeDao, UserActivityDao}
import org.cunxin.reward.app.model.UserEventType
import org.cunxin.reward.app.model.reward.points._
import org.cunxin.support.util.{HttpDataStrategyImpl, HttpDataStrategy}
import org.apache.http.client.HttpClient
import org.apache.http.params.{CoreConnectionPNames, BasicHttpParams}
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager
import org.cunxin.reward.app.module.{PointsInstancesModule, BadgersInstancesModule}

@Test
class PointsSystemTest extends EmbedDb {

  private[this] val module = new ScalaModule {
    def configure() {
      install(new BadgersInstancesModule)
      install(new PointsInstancesModule)

      bind[HttpClient].toInstance(createHttpClient)
      bind[HttpDataStrategyImpl].asEagerSingleton()
      bind[HttpDataStrategy].to[HttpDataStrategyImpl]

      bind[UserActivityDao].asEagerSingleton()
      bind[UserAllTimeDao].asEagerSingleton()
      bind[UserInfoDao].asEagerSingleton()

      bind[UserEventService].asEagerSingleton()
      bind[UserRewardService].asEagerSingleton()
      bind[UserInfoService].asEagerSingleton()
    }

    private[this] def createHttpClient: HttpClient = {
      val params = new BasicHttpParams
      params.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60 * 1000) //1 minute
      new DefaultHttpClient(createConnectionManager, params)
    }

    private[this] def createConnectionManager: ThreadSafeClientConnManager = {
      val m = new ThreadSafeClientConnManager()
      m.setDefaultMaxPerRoute(10)
      m.setMaxTotal(10000)
      m
    }
  }

  def testSupportPoints() {
    val injector = new ScalaInjector(Guice.createInjector(module, mongoModule))
    val userEventService = injector.instance[UserEventService]
    val userInfoService = injector.instance[UserInfoService]
    val supportPoints = injector.instance[SupportPoints]

    val userId = "userId1"
    val projectId = "projectId1"

    val result1 = userEventService.recordEvent(userId, projectId, UserEventType.SUPPORT, Map())
    val result2 = userEventService.recordEvent(userId, projectId, UserEventType.SUPPORT, Map())
    val result3 = userEventService.recordEvent(userId, projectId, UserEventType.SUPPORT, Map())
    val result4 = userEventService.recordEvent(userId, projectId, UserEventType.SUPPORT, Map())
    val result5 = userEventService.recordEvent(userId, projectId, UserEventType.SUPPORT, Map())
    val result6 = userEventService.recordEvent(userId, projectId, UserEventType.SUPPORT, Map())

    Assert.assertEquals(result1(supportPoints.id), "1")
    Assert.assertEquals(result2(supportPoints.id), "1")
    Assert.assertEquals(result3(supportPoints.id), "1")
    Assert.assertEquals(result4(supportPoints.id), "1")
    Assert.assertEquals(result5(supportPoints.id), "1")
    Assert.assertEquals(result6(supportPoints.id), "0")

    Assert.assertEquals(userInfoService.getPoints(userId), 5)

  }

  def testDonationPoints() {
    val injector = new ScalaInjector(Guice.createInjector(module, mongoModule))
    val userEventService = injector.instance[UserEventService]
    val userInfoService = injector.instance[UserInfoService]
    val donationPoints = injector.instance[DonationPoints]

    val userId = "userId2"
    val projectId = "projectId2"

    val result1 = userEventService.recordEvent(userId, projectId, UserEventType.DONATION, Map("amount" -> List("500")))
    val result2 = userEventService.recordEvent(userId, projectId, UserEventType.DONATION, Map("amount" -> List("500")))

    Assert.assertEquals(result1(donationPoints.id), "501")
    Assert.assertEquals(result2(donationPoints.id), "500")

    Assert.assertEquals(userInfoService.getPoints(userId), 1001)

  }

  def testDonationSharePoints() {
    val injector = new ScalaInjector(Guice.createInjector(module, mongoModule))
    val userEventService = injector.instance[UserEventService]
    val userInfoService = injector.instance[UserInfoService]
    val shareAfterDonationPoints = injector.instance[ShareAfterDonationPoints]

    val userId = "userId3"
    val projectId = "projectId3"

    val result1 = userEventService.recordEvent(userId, projectId, UserEventType.DONATION_SHARE, Map())
    val result2 = userEventService.recordEvent(userId, projectId, UserEventType.DONATION_SHARE, Map())

    Assert.assertEquals(result1(shareAfterDonationPoints.id), "4")
    Assert.assertEquals(result2(shareAfterDonationPoints.id), "0")

    Assert.assertEquals(userInfoService.getPoints(userId), 4)

  }
}
