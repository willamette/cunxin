package org.cunxin.reward.api.module

import uk.me.lings.scalaguice.ScalaModule
import org.cunxin.reward.app.model.reward.points._
import java.util.concurrent.{Executors, ScheduledExecutorService}

class RewardInstancesModule extends ScalaModule {
  def configure() {
    bind[ShareAfterDonationPoints].asEagerSingleton()
    bind[SupportPoints].asEagerSingleton()
    bind[ShareToWeiBoPoints].asEagerSingleton()
    bind[DonationPoints].asEagerSingleton()

    bind[ScheduledExecutorService].toInstance(Executors.newScheduledThreadPool(4, Executors.defaultThreadFactory()))
  }
}
