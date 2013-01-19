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
import java.util.Date

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

  lazy val injector = new ScalaInjector(Guice.createInjector(module, mongoModule))

  def testSupportPointsWithSameProjectIds() {
    val userEventService = injector.instance[UserEventService]
    val userInfoService = injector.instance[UserInfoService]
    val supportPoints = injector.instance[SupportPoints]

    val userId = "userId1_1"
    val projectId = "projectId1"

    val today = new Date()
    val result1 = userEventService.recordEvent(userId, projectId, today, UserEventType.SUPPORT, Map())
    val result2 = userEventService.recordEvent(userId, projectId, today, UserEventType.SUPPORT, Map())
    val result3 = userEventService.recordEvent(userId, projectId, today, UserEventType.SUPPORT, Map())
    val result4 = userEventService.recordEvent(userId, projectId, today, UserEventType.SUPPORT, Map())
    val result5 = userEventService.recordEvent(userId, projectId, today, UserEventType.SUPPORT, Map())
    val result6 = userEventService.recordEvent(userId, projectId, today, UserEventType.SUPPORT, Map())

    Assert.assertEquals(result1(supportPoints.id), "1")
    Assert.assertEquals(result2(supportPoints.id), "0")
    Assert.assertEquals(result3(supportPoints.id), "0")
    Assert.assertEquals(result4(supportPoints.id), "0")
    Assert.assertEquals(result5(supportPoints.id), "0")
    Assert.assertEquals(result6(supportPoints.id), "0")

    Assert.assertEquals(userInfoService.getPoints(userId), 1)

  }

  def testSupportPointsWithDiffProjectIds() {
    val userEventService = injector.instance[UserEventService]
    val userInfoService = injector.instance[UserInfoService]
    val supportPoints = injector.instance[SupportPoints]

    val userId = "userId1_2"
    val projectId1 = "projectId1_1"
    val projectId2 = "projectId1_2"
    val projectId3 = "projectId1_3"
    val projectId4 = "projectId1_4"
    val projectId5 = "projectId1_5"
    val projectId6 = "projectId1_6"

    val today = new Date
    val result1 = userEventService.recordEvent(userId, projectId1, today, UserEventType.SUPPORT, Map())
    val result2 = userEventService.recordEvent(userId, projectId2, today, UserEventType.SUPPORT, Map())
    val result3 = userEventService.recordEvent(userId, projectId3, today, UserEventType.SUPPORT, Map())
    val result4 = userEventService.recordEvent(userId, projectId4, today, UserEventType.SUPPORT, Map())
    val result5 = userEventService.recordEvent(userId, projectId5, today, UserEventType.SUPPORT, Map())
    val result6 = userEventService.recordEvent(userId, projectId6, today, UserEventType.SUPPORT, Map())

    Assert.assertEquals(result1(supportPoints.id), "1")
    Assert.assertEquals(result2(supportPoints.id), "1")
    Assert.assertEquals(result3(supportPoints.id), "1")
    Assert.assertEquals(result4(supportPoints.id), "1")
    Assert.assertEquals(result5(supportPoints.id), "1")
    Assert.assertEquals(result6(supportPoints.id), "0")

    Assert.assertEquals(userInfoService.getPoints(userId), 5)

  }

  def testDonationPoints() {
    val userEventService = injector.instance[UserEventService]
    val userInfoService = injector.instance[UserInfoService]
    val donationPoints = injector.instance[DonationPoints]

    val userId = "userId2"
    val projectId = "projectId2"

    val today = new Date
    val result1 = userEventService.recordEvent(userId, projectId, today, UserEventType.DONATION, Map("amount" -> List("500")))
    val result2 = userEventService.recordEvent(userId, projectId, today, UserEventType.DONATION, Map("amount" -> List("500")))

    Assert.assertEquals(result1(donationPoints.id), "503")
    Assert.assertEquals(result2(donationPoints.id), "500")

    Assert.assertEquals(userInfoService.getPoints(userId), 1003)

  }

  def testDonationSharePoints() {
    val userEventService = injector.instance[UserEventService]
    val userInfoService = injector.instance[UserInfoService]
    val shareAfterDonationPoints = injector.instance[ShareAfterDonationPoints]

    val userId = "userId2"
    val projectId = "projectId2"

    val today = new Date
    val result1 = userEventService.recordEvent(userId, projectId, today, UserEventType.DONATION_SHARE, Map())
    val result2 = userEventService.recordEvent(userId, projectId, today, UserEventType.DONATION_SHARE, Map())

    Assert.assertEquals(result1(shareAfterDonationPoints.id), "2")
    Assert.assertEquals(result2(shareAfterDonationPoints.id), "0")

    Assert.assertEquals(userInfoService.getPoints(userId), 1005)

  }


  def testLoginPointsAtSameDay() {
    val userEventService = injector.instance[UserEventService]
    val userInfoService = injector.instance[UserInfoService]
    val loginPoints = injector.instance[LoginPoints]

    val userId = "userId3"
    val projectId = "projectId3"

    val result1 = userEventService.recordEvent(userId, projectId, new Date, UserEventType.LOGIN, Map())
    val result2 = userEventService.recordEvent(userId, projectId, new Date, UserEventType.LOGIN, Map())

    Assert.assertEquals(result1(loginPoints.id), "1")
    Assert.assertEquals(result2(loginPoints.id), "0")

    Assert.assertEquals(userInfoService.getPoints(userId), 1)

  }

  def testLoginPointsAtThreeContinuousDays() {
    val userEventService = injector.instance[UserEventService]
    val userInfoService = injector.instance[UserInfoService]
    val loginPoints = injector.instance[LoginPoints]

    val userId = "userId4"
    val projectId = "projectId4"

    val day3 = new Date
    val day2 = new Date(day3.getTime - 1000 * 60 * 60 * 24)
    val day1 = new Date(day3.getTime - 1000 * 60 * 60 * 48)
    val result1 = userEventService.recordEvent(userId, projectId, day1, UserEventType.LOGIN, Map())
    val result2 = userEventService.recordEvent(userId, projectId, day2, UserEventType.LOGIN, Map())
    val result3 = userEventService.recordEvent(userId, projectId, day3, UserEventType.LOGIN, Map())

    Assert.assertEquals(result1(loginPoints.id), "1")
    Assert.assertEquals(result2(loginPoints.id), "1")
    Assert.assertEquals(result3(loginPoints.id), "2")

    Assert.assertEquals(userInfoService.getPoints(userId), 4)

  }

  def testLoginPointsAtThreeContinuousDaysAndOneDayGap() {
    val userEventService = injector.instance[UserEventService]
    val userInfoService = injector.instance[UserInfoService]
    val loginPoints = injector.instance[LoginPoints]

    val userId = "userId5"
    val projectId = "projectId5"

    val day4 = new Date
    val day3 = new Date(day4.getTime - 1000 * 60 * 60 * 60)
    val day2 = new Date(day3.getTime - 1000 * 60 * 60 * 24)
    val day1 = new Date(day3.getTime - 1000 * 60 * 60 * 48)
    val result1 = userEventService.recordEvent(userId, projectId, day1, UserEventType.LOGIN, Map())
    val result2 = userEventService.recordEvent(userId, projectId, day2, UserEventType.LOGIN, Map())
    val result3 = userEventService.recordEvent(userId, projectId, day3, UserEventType.LOGIN, Map())
    val result4 = userEventService.recordEvent(userId, projectId, day4, UserEventType.LOGIN, Map())

    Assert.assertEquals(result1(loginPoints.id), "1")
    Assert.assertEquals(result2(loginPoints.id), "1")
    Assert.assertEquals(result3(loginPoints.id), "2")
    Assert.assertEquals(result4(loginPoints.id), "1")

    Assert.assertEquals(userInfoService.getPoints(userId), 5)

  }
}
