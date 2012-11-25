package org.cunxin.reward.app.model.reward

import com.google.inject.Inject

abstract class Badger extends Reward {
  @Inject
  def register() {
    ProcessChain.registerBadger(this)
  }
}
