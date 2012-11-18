package org.cunxin.reward.app.service

import org.testng.Assert
import com.google.inject.Guice
import org.testng.annotations.Test
import uk.me.lings.scalaguice.ScalaModule
import uk.me.lings.scalaguice.InjectorExtensions.ScalaInjector
import org.cunxin.support.sandbox.EmbedDb
import org.cunxin.reward.app.dao.{UserAllTimeDao, UserActivityDao}
import org.cunxin.reward.app.model.UserEventType
import org.cunxin.reward.app.model.reward.points._

@Test
class RewardSystemTest extends EmbedDb {

    private[this] val module = new ScalaModule {
        def configure() {
            bind[UserActivityDao].asEagerSingleton()
            bind[UserAllTimeDao].asEagerSingleton()

            bind[UserEventService].asEagerSingleton()
            bind[UserRewardService].asEagerSingleton()

            bind[ShareAfterDonationPoints].asEagerSingleton()
            bind[SupportPoints].asEagerSingleton()
            bind[ShareToWeiBoPoints].asEagerSingleton()
            bind[DonationPoints].asEagerSingleton()
        }
    }

    def testSupportPoints() {
        val injector = new ScalaInjector(Guice.createInjector(module, mongoModule))
        val userEventService = injector.instance[UserEventService]
        val supportPoints = injector.instance[SupportPoints]

        val userId = "userId"
        val projectId = "projectId"

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

    }

    def testDonationPoints() {
        val injector = new ScalaInjector(Guice.createInjector(module, mongoModule))
        val userEventService = injector.instance[UserEventService]
        val donationPoints = injector.instance[DonationPoints]

        val userId = "userId"
        val projectId = "projectId"

        val result1 = userEventService.recordEvent(userId, projectId, UserEventType.DONATION, Map("amount" -> List("500")))
        val result2 = userEventService.recordEvent(userId, projectId, UserEventType.DONATION, Map("amount" -> List("500")))

        Assert.assertEquals(result1(donationPoints.id), "501")
        Assert.assertEquals(result2(donationPoints.id), "500")

    }

    def testDonationSharePoints() {
        val injector = new ScalaInjector(Guice.createInjector(module, mongoModule))
        val userEventService = injector.instance[UserEventService]
        val shareAfterDonationPoints = injector.instance[ShareAfterDonationPoints]

        val userId = "userId"
        val projectId = "projectId"

        val result1 = userEventService.recordEvent(userId, projectId, UserEventType.DONATION_SHARE, Map())
        val result2 = userEventService.recordEvent(userId, projectId, UserEventType.DONATION_SHARE, Map())

        Assert.assertEquals(result1(shareAfterDonationPoints.id), "1")
        Assert.assertEquals(result2(shareAfterDonationPoints.id), "0")

    }
}