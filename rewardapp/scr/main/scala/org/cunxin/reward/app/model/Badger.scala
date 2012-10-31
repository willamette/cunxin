package org.cunxin.reward.app.model

trait Badger {
  def id: String
  def recordEvent()
  def isQualified: Boolean
  def getStat: Map[UserEventType, Int]
}
