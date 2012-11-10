package org.cunxin.reward.app.dao

import com.google.inject.Inject
import com.mongodb.casbah.MongoDB
import org.cunxin.support.db.{MongoRWDao, AbstractMongoDao}
import org.cunxin.reward.app.model.Points

class PointsDao @Inject()(db: MongoDB) extends AbstractMongoDao[Points](db, "points", version = 1) with MongoRWDao[Points] {
    ensureIndex("pointsId" -> 1, "idx_pointsId", unique = true)

    def findPointsById(id: String) = find("pointsId" -> id).headOption
}
