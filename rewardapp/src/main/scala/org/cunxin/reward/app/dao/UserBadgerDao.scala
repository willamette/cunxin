package org.cunxin.reward.app.dao

import com.google.inject.Inject
import com.mongodb.casbah.MongoDB
import org.cunxin.support.db.{MongoRWDao, AbstractMongoDao}
import org.cunxin.reward.app.model.UserBadger

class UserBadgerDao @Inject()(db: MongoDB) extends AbstractMongoDao[UserBadger](db, "userbadger", version = 1) with MongoRWDao[UserBadger] {
  ensureIndex("userId" -> 1, "idx_userId", unique = false)

  def findUserBadgersByUser(userId: String) = find("userId" -> userId)

}
