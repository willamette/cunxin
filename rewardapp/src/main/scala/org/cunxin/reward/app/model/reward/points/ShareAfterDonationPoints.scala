package org.cunxin.reward.app.model.reward.points

import collection.mutable
import org.cunxin.reward.app.model.{EventStats, UserActivity, UserAllTimeStats, UserEventType}
import org.cunxin.reward.app.model.reward.Points
import org.apache.http.client.methods.HttpGet
import java.util.concurrent.{ScheduledExecutorService, TimeUnit}
import com.google.inject.Inject
import org.cunxin.support.util.HttpDataStrategy
import org.cunxin.reward.app.service.UserInfoService

class ShareAfterDonationPoints @Inject()(httpDataStrategy: HttpDataStrategy, userInfoService: UserInfoService, scheduler: ScheduledExecutorService) extends Points {
  def id = "shareAfterDonationPoints"

  def observingEvents = List(UserEventType.DONATION_SHARE)

  def onPublish(userId: String, projectId: String, eventType: UserEventType, data: Map[String, List[String]],
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
        userInfoService.updateRewards(userId, id, points.toString)
      }
    }, 167, TimeUnit.HOURS)
    if (projectStats == 0) 2 else 0
  }
}
