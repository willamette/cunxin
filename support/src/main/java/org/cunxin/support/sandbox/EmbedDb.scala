package org.cunxin.support.sandbox

import de.flapdoodle.embedmongo.config.MongodConfig
import de.flapdoodle.embedmongo.distribution.Version
import de.flapdoodle.embedmongo.{MongodExecutable, MongodProcess, MongoDBRuntime}
import com.mongodb.casbah.MongoDB
import java.net.ServerSocket
import org.testng.annotations.{AfterSuite, BeforeClass}
import org.cunxin.support.util.ConfigModule

abstract class EmbedDb {
  private[this] val mongoRuntime = MongoDBRuntime.getDefaultInstance
  private[this] var mongoExec: Option[MongodExecutable] = None
  private[this] var mongoProc: Option[MongodProcess] = None

  private[this] val mongoPort = findOpenPort()

  protected def findOpenPort(): Int = {
    val tmpSock = new ServerSocket(0)
    val port = tmpSock.getLocalPort
    tmpSock.close()
    port
  }

  protected val mongoModule = new ConfigModule {
    def configure() {
      config("mongo.port", mongoPort)
      bind[MongoDB].toProvider[EmbedMongoDbProvider].asEagerSingleton()
    }
  }

  @BeforeClass(alwaysRun = true)
  protected def startMongoDb() {
      mongoExec = Option(mongoRuntime.prepare(new MongodConfig(Version.V2_0_5, mongoPort, false)))
      mongoExec match {
        case Some(exec) => {
          mongoProc = Option(exec.start())
        }
        case None =>
      }
  }

  @AfterSuite(alwaysRun = true)
  protected def stopMongoDb() {
    mongoProc match {
      case Some(proc) =>{
        proc.stop()
      }
      case None =>
    }
    mongoExec match {
      case Some(exec) => exec.cleanup()
      case None =>
    }
    mongoProc = None
    mongoExec = None
  }
}
