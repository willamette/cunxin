package org.cunxin.support.util

import com.google.inject.Inject
import org.apache.http.client.HttpClient
import org.apache.commons.logging.LogFactory
import org.apache.http.client.methods.HttpUriRequest
import java.io.InputStream
import org.apache.http.HttpStatus
import java.text.MessageFormat

trait HttpDataStrategy {
    def executeMethod[R: Manifest](httpMethod: HttpUriRequest): Option[R]
    def executeMethod[R: Manifest](httpMethod: HttpUriRequest, parse: (HttpParserParam) => Option[R]): Option[R]
}

case class HttpParserParam(url: String, stream: InputStream)

sealed abstract class AbstractHttpDataStrategy(jackson: JsonSerDe) extends HttpDataStrategy {
    protected def deserialize[R: Manifest](stream: InputStream): Option[R] = {
        Some(jackson.deserialize[R](stream))
    }
}

class HttpDataStrategyImpl @Inject()(httpClient: HttpClient, jackson: JsonSerDe) extends AbstractHttpDataStrategy(jackson) {
    private[this] val log = LogFactory.getLog(this.getClass)


    def executeMethod[R: Manifest](httpMethod: HttpUriRequest): Option[R] = {
        executeMethod[R](httpMethod, (param: HttpParserParam) => deserialize[R](param.stream))
    }

    def executeMethod[R: Manifest](httpMethod: HttpUriRequest, parse: (HttpParserParam) => Option[R]): Option[R] = {
        var stream: Option[InputStream] = None

        try {
            val response = httpClient.execute(httpMethod)
            val status = response.getStatusLine.getStatusCode
            stream = Option(response.getEntity.getContent)

            if (status != HttpStatus.SC_OK) {
                log.warn(MessageFormat.format("Http Error: code: {0}", String.valueOf(status)))
                return None
            }

            parse(HttpParserParam(httpMethod.getURI.toString, stream.get))

        } finally {
            // Closing the input stream will trigger connection release
            try {
                stream.foreach(_.close())
            } catch {
                case e: Exception => log.warn("Cannot close http connection: ", e)
            }
        }
    }
}