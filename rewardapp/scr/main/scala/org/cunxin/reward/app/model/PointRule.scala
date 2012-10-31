package org.cunxin.reward.app.model

trait PointRule {
  def id: String
  def recordEvent()
  def getPoints: Int
}