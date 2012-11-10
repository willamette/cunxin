package org.cunxin.reward.app.dao

import com.google.inject.Inject
import com.mongodb.casbah.MongoDB
import org.cunxin.support.db.{MongoRWDao, AbstractMongoDao}
import org.cunxin.reward.app.model.Badger

class BadgerDao @Inject()(db: MongoDB) extends AbstractMongoDao[Badger](db, "badger", version = 1) with MongoRWDao[Badger] {
  ensureIndex("badgerId" -> 1, "idx_badgerId", unique = true)

  def findBadgerById(id: String) = find("badgerId" -> id).headOption
}