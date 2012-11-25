package org.cunxin.reward.app.model.reward

import com.google.inject.Inject

abstract class Points extends Reward {
  @Inject
  def register() {
    ProcessChain.registerPoints(this)
  }
}
