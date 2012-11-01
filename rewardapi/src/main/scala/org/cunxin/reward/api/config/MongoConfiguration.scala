package org.cunxin.reward.api.config

import com.yammer.dropwizard.config.Configuration

case class MongoConfiguration(host: String, port: Int, user: String, password: String, database: String) extends Configuration
