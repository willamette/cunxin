package org.cunxin.reward.app.model.reward.points

import com.google.inject.Inject
import org.cunxin.support.util.HttpDataStrategy
import org.cunxin.reward.app.model.{UserActivity, UserAllTimeStats, UserEventType}
import org.apache.http.client.methods.HttpGet
import org.cunxin.reward.app.service.UserInfoService
import java.util.concurrent.{TimeUnit, ScheduledExecutorService}
import org.cunxin.reward.app.model.reward.Points
import java.util.Date
import org.apache.commons.logging.LogFactory

case class WeiBoResponse(id: String, comments: String, reposts: String)

class ShareToWeiBoPoints @Inject()(httpDataStrategy: HttpDataStrategy, userInfoService: UserInfoService, scheduler: ScheduledExecutorService) extends Points {

  private[this] val logger = LogFactory.getLog(this.getClass)

  def id = "shareToWeiBoPoints"

  def observingEvents = List(UserEventType.WEIBO_SHARE)

  def onPublish(userId: String, projectId: String, date: Date, eventType: UserEventType, data: Map[String, List[String]],
                pastAllTimeStats: UserAllTimeStats, pastActivities: List[UserActivity]): Int = {
    if (pastAllTimeStats.projectStats.contains(projectId) && pastAllTimeStats.projectStats(projectId).stats.contains(UserEventType.WEIBO_SHARE)) {
      logger.info("User %s has shared project %s to weibo before".format(userId, projectId))
      return 0
    }
    scheduler.schedule(new Runnable {
      def run() {
        val url = "http://api.weibo.com/2/statuses/count.json?ids=%s&access_token=%s".format(data("id")(0), data("token")(0))
        val result = httpDataStrategy.executeMethod[List[WeiBoResponse]](new HttpGet(url))
        val reposts = result.getOrElse(return 0).head.reposts.toInt
        //        if (reposts == "100") {
        //          userInfoService.updateRewards(userId, ZhongZhiChengChengBadger.id, "1")
        //        }
        val points = if (reposts > 300) 10 else reposts / 10
        logger.info("User %s's shared weibo for project %s has been reposted %d times, points %d".format(userId, projectId, reposts, points))
        userInfoService.updateRewards(userId, id, points.toString)
      }
    }, 167, TimeUnit.HOURS)
    1
  }
}
