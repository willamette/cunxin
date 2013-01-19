package org.cunxin.reward.app.module

import uk.me.lings.scalaguice.ScalaModule
import org.cunxin.reward.app.model.reward.points._
import java.util.concurrent.{Executors, ScheduledExecutorService}

class PointsInstancesModule extends ScalaModule {
  def configure() {
    bind[ShareAfterDonationPoints].asEagerSingleton()
    bind[SupportPoints].asEagerSingleton()
    bind[ShareToWeiBoPoints].asEagerSingleton()
    bind[DonationPoints].asEagerSingleton()
    bind[LoginPoints].asEagerSingleton()

    bind[ScheduledExecutorService].toInstance(Executors.newScheduledThreadPool(4, Executors.defaultThreadFactory()))
  }
}
