package org.cunxin.reward.app.model.reward

import com.google.inject.Inject
import org.cunxin.reward.app.ProcessChain

abstract class Points extends Reward {
  @Inject
  def register() {
    ProcessChain.registerPoints(this)
  }
}
