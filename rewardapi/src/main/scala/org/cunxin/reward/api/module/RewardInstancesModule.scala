package org.cunxin.reward.api.module

import uk.me.lings.scalaguice.ScalaModule
import org.cunxin.reward.app.model.reward.points._

class RewardInstancesModule extends ScalaModule {
  def configure() {
    bind[ShareAfterDonationPoints].asEagerSingleton()
    bind[SupportPoints].asEagerSingleton()
    bind[ShareToWeiBoPoints].asEagerSingleton()
    bind[DonationPoints].asEagerSingleton()
  }
}
