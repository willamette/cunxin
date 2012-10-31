package org.cunxin.support.db

trait DBManagedModel[T] {
  def data: T
  def copy(data: T): DBManagedModel[T]
}
