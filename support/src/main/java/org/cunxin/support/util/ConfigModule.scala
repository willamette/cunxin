package org.cunxin.support.util

import uk.me.lings.scalaguice.ScalaModule
import com.google.inject.name.Names

abstract class ConfigModule extends ScalaModule {
  protected def config[T: Manifest](name: String, t: T) {
    bind[T].annotatedWith(Names.named(name)).toInstance(t)
  }
}