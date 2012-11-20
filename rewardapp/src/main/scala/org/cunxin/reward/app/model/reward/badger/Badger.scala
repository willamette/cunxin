package org.cunxin.reward.app.model.reward.badger

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import org.cunxin.reward.app.model.reward.{ProcessChain, Reward}
import com.google.inject.Inject

@JsonIgnoreProperties(ignoreUnknown = true)
abstract class Badger(id: String, name: String) extends Reward {
  @Inject
  def register() {
    ProcessChain.registerBadger(this)
  }
}
