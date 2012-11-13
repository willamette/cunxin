package org.cunxin.reward.app.service

import com.google.inject.Guice
import org.testng.annotations.Test
import uk.me.lings.scalaguice.ScalaModule
import uk.me.lings.scalaguice.InjectorExtensions.ScalaInjector
import org.cunxin.support.sandbox.EmbedDb

@Test
class RewardSystemTest extends EmbedDb {

    private[this] val module = new ScalaModule {
        def configure() {

            bind[UserEventService].asEagerSingleton()
            bind[UserRewardService].asEagerSingleton()
        }
    }

    def test() {
        val injector = new ScalaInjector(Guice.createInjector(module, mongoModule))

        val userEventService = injector.instance[UserEventService]
        val userRewardService = injector.instance[UserRewardService]


    }

}
