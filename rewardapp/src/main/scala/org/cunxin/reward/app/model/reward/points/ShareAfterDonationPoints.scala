package org.cunxin.reward.app.model.reward.points

import collection.mutable
import org.cunxin.reward.app.model.{EventStats, UserActivity, UserAllTimeStats, UserEventType}
import org.cunxin.reward.app.model.reward.Points
import org.apache.http.client.methods.HttpGet
import java.util.concurrent.{ScheduledExecutorService, TimeUnit}
import com.google.inject.Inject
import org.cunxin.support.util.HttpDataStrategy
import org.cunxin.reward.app.service.UserInfoService
import java.util.Date
import org.apache.commons.logging.LogFactory

class ShareAfterDonationPoints @Inject()(httpDataStrategy: HttpDataStrategy, userInfoService: UserInfoService, scheduler: ScheduledExecutorService) extends Points {

  private[this] val logger = LogFactory.getLog(this.getClass)

  def id = "shareAfterDonationPoints"

  def observingEvents = List(UserEventType.DONATION_SHARE)

  def onPublish(userId: String, projectId: String, date: Date, eventType: UserEventType, data: Map[String, List[String]],
                pastAllTimeStats: UserAllTimeStats, pastActivities: List[UserActivity]): Int = {
    val projectStats = pastAllTimeStats.projectStats.getOrElse(projectId, EventStats(new mutable.HashMap[UserEventType, Int]())).stats.getOrElse(UserEventType.DONATION_SHARE, 0)
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
    if (projectStats == 0) {
      logger.info("User %s hasn't shared this project yet, getting 2 points".format(userId))
      2
    } else {
      logger.info("User %s has shared this project before, getting 0 points".format(userId))
      0
    }
  }
}
