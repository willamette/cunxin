package org.cunxin.reward.api.server

import com.google.common.cache.CacheBuilderSpec
import com.google.inject.Guice
import uk.me.lings.scalaguice.InjectorExtensions.ScalaInjector
import com.yammer.dropwizard.config.Environment
import org.cunxin.reward.api.module.RewardApiModule
import org.cunxin.reward.api.config.MongoConfiguration
import org.cunxin.reward.api.service.{RewardApiServiceResource, ScalaService}

object RewardApiServer extends ScalaService[MongoConfiguration]("Reward Api Server") {
  val cacheSpec = CacheBuilderSpec.disableCaching()

  def initialize(mongoConfig: MongoConfiguration, env: Environment) {
    val injector = new ScalaInjector(Guice.createInjector(new RewardApiModule(mongoConfig)))
    env.addResource(injector.instance[RewardApiServiceResource])
  }
}
