package org.cunxin.support.sandbox

import com.google.inject.name.Named
import com.bazaarvoice.ads.support.util.Port
import com.google.inject.{Provider, Inject}
import com.mongodb.casbah.{MongoDB, MongoConnection}

class EmbedMongoDbProvider @Inject()(@Named("mongo.port") port: Int) extends Provider[MongoDB] {
  def get(): MongoDB = {
    MongoConnection("localhost", port)("qa-tests")
  }
}
