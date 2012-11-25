package org.cunxin.reward.app.module

import uk.me.lings.scalaguice.ScalaModule
import org.cunxin.reward.app.model.reward.badger._

class BadgersInstancesModule extends ScalaModule {
  def configure() {
    bind[AiDeHuHuanBadger].asEagerSingleton()
    bind[DaAiWuJiangBadger].asEagerSingleton()
    bind[ShiYuZuXiaBadger].asEagerSingleton()
    bind[YiJinMianBoBadger].asEagerSingleton()
    bind[YiWangErShengBadger].asEagerSingleton()
  }
}
