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

        userEventService.recordEvent(userId, projectId, UserEventType.SUPPORT, Map())
        userEventService.recordEvent(userId, projectId, UserEventType.SUPPORT, Map())
        userEventService.recordEvent(userId, projectId, UserEventType.SUPPORT, Map())
        userEventService.recordEvent(userId, projectId, UserEventType.SUPPORT, Map())
        val result = userEventService.recordEvent(userId, projectId, UserEventType.SUPPORT, Map())
        Assert.assertEquals(result(supportPoints.id), "1")
        val result2 = userEventService.recordEvent(userId, projectId, UserEventType.SUPPORT, Map())
        Assert.assertEquals(result2(supportPoints.id), "0")

    }

}
