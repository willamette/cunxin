package org.cunxin.reward.api.module

import org.cunxin.reward.app.service.{UserInfoService, UserRewardService, UserEventService}
import uk.me.lings.scalaguice.ScalaModule
import com.google.inject.Provider
import com.mongodb.casbah.{MongoConnection, MongoDB}
import org.apache.commons.logging.LogFactory
import org.cunxin.reward.api.config.MongoConfiguration
import org.cunxin.reward.app.dao.{UserInfoDao, UserAllTimeDao, UserActivityDao}

class RewardApiModule(mongoConfig: MongoConfiguration) extends ScalaModule {
    def configure() {

        bind[MongoDB].toInstance(MongoDBProvider(mongoConfig).get())

        bind[UserActivityDao].asEagerSingleton()
        bind[UserAllTimeDao].asEagerSingleton()
        bind[UserInfoDao].asEagerSingleton()

        bind[UserEventService].asEagerSingleton()
        bind[UserRewardService].asEagerSingleton()
        bind[UserInfoService].asEagerSingleton()
    }
}

case class MongoDBProvider(authMongo: MongoConfiguration) extends Provider[MongoDB] {
    private[this] val log = LogFactory.getLog(this.getClass)

    def get(): MongoDB = {
        val db = MongoConnection(authMongo.host, authMongo.port)(authMongo.database)
        db.authenticate(authMongo.user, authMongo.password)

        log.info("MongoDB connection instantiated: %s@%s:%d (%s)".format(authMongo.user, authMongo.host, authMongo.port, authMongo.database))
        db
    }
}
