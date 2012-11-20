package org.cunxin.reward.api.module

import uk.me.lings.scalaguice.ScalaModule
import org.apache.http.client.HttpClient
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.params.{BasicHttpParams, CoreConnectionPNames}
import org.cunxin.support.util.{HttpDataStrategy, HttpDataStrategyImpl}

class HttpStrategyModule extends ScalaModule {

  def configure() {
    // Data Sources
    bind[HttpClient].toInstance(createHttpClient)
    bind[HttpDataStrategyImpl].asEagerSingleton()
    bind[HttpDataStrategy].to[HttpDataStrategyImpl]
  }

  private[this] def createHttpClient: HttpClient = {
    val params = new BasicHttpParams
    params.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60 * 1000) //1 minute
    new DefaultHttpClient(createConnectionManager, params)
  }

  private[this] def createConnectionManager: ThreadSafeClientConnManager = {
    val m = new ThreadSafeClientConnManager()
    m.setDefaultMaxPerRoute(10)
    m.setMaxTotal(10000)
    m
  }
}