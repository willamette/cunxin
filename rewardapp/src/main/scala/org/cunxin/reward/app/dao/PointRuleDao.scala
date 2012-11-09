package org.cunxin.reward.app.dao

import com.google.inject.Inject
import com.mongodb.casbah.MongoDB
import org.cunxin.support.db.{MongoRWDao, AbstractMongoDao}
import org.cunxin.reward.app.model.PointRule


class PointRuleDao @Inject()(db: MongoDB) extends AbstractMongoDao[PointRule](db, "pointrule", version = 1) with MongoRWDao[PointRule] {
    ensureIndex("pointRuleId" -> 1, "idx_pointRuleId", unique = true)

    def findPointRuleById(id: String) = find("pointRuleId" -> id).headOption
}
