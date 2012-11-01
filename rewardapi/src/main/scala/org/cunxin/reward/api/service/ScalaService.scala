package org.cunxin.reward.api.service

import com.yammer.dropwizard.config.{Environment, Configuration}
import com.yammer.dropwizard.{ConfiguredBundle, Bundle, AbstractService}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.yammer.dropwizard.jersey.JacksonMessageBodyProvider
import com.codahale.jersey.inject.ScalaCollectionsQueryParamInjectableProvider

abstract class ScalaService[T <: Configuration](name: String) extends AbstractService[T](name) {
  addBundle(new ScalaBundle(this))
  addJacksonModule(DefaultScalaModule)

  override final def subclassServiceInsteadOfThis() {}

  final def main(args: Array[String]) {
    run(args)
  }

  def withBundle(bundle: Bundle) {
    addBundle(bundle)
  }

  def withBundle(bundle: ConfiguredBundle[_ >: T]) {
    addBundle(bundle)
  }
}

class ScalaBundle(service: ScalaService[_ <: Configuration]) extends Bundle {
  def initialize(environment: Environment) {
    environment.addProvider(new JacksonMessageBodyProvider(service.getJson))
    environment.addProvider(new ScalaCollectionsQueryParamInjectableProvider())
  }
}
