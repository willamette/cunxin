package org.cunxin.reward.app.dao

import com.google.inject.Inject
import com.mongodb.casbah.MongoDB
import org.cunxin.support.db.{MongoRWDao, AbstractMongoDao}
import org.cunxin.reward.app.model.UserAllTimeStats

class UserAllTimeDao @Inject()(db: MongoDB) extends AbstractMongoDao[UserAllTimeStats](db, "useralltimestats", version = 1)
with MongoRWDao[UserAllTimeStats] {
    ensureIndex("userId" -> 1, "idx_userId", unique = true)

    def findUserAllTimeStatsByUserId(id: String) = find("userId" -> id).headOption

}
