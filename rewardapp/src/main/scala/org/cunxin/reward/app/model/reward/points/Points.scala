package org.cunxin.reward.app.model.reward.points

import com.google.inject.Inject
import org.cunxin.reward.app.model.reward.{ProcessChain, Reward}

abstract class Points extends Reward {
  @Inject
  def register() {
    ProcessChain.registerPoints(this)
  }
}
