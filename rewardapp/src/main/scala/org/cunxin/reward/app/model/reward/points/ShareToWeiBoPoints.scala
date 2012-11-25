package org.cunxin.reward.app.model.reward.points

import com.google.inject.Inject
import org.cunxin.support.util.HttpDataStrategy
import org.cunxin.reward.app.model.{UserActivity, UserAllTimeStats, UserEventType}
import org.apache.http.client.methods.HttpGet
import org.cunxin.reward.app.service.UserInfoService
import java.util.concurrent.{TimeUnit, ScheduledExecutorService}
import org.cunxin.reward.app.model.reward.Points
import org.cunxin.reward.app.model.reward.badger.ZhongZhiChengChengBadger

case class WeiBoResponse(id: String, comments: String, reposts: String)

class ShareToWeiBoPoints @Inject()(httpDataStrategy: HttpDataStrategy, userInfoService: UserInfoService, scheduler: ScheduledExecutorService) extends Points {
  def id = "shareToWeiBoPoints"

  def observingEvents = List(UserEventType.WEIBO_SHARE)

  def onPublish(userId: String, projectId: String, eventType: UserEventType, data: Map[String, List[String]],
                pastAllTimeStats: UserAllTimeStats, pastActivities: List[UserActivity]): Int = {
    if (pastAllTimeStats.projectStats.contains(projectId) && pastAllTimeStats.projectStats(projectId).stats.contains(UserEventType.WEIBO_SHARE))
      return 0
    scheduler.schedule(new Runnable {
      def run() {
        val url = "http://api.weibo.com/2/statuses/count.json?ids=%s&access_token=%s".format(data("id")(0), data("token")(0))
        val result = httpDataStrategy.executeMethod[List[WeiBoResponse]](new HttpGet(url))
        val reposts = result.getOrElse(return 0).head.reposts
        if (reposts == "100") {
          userInfoService.updateRewards(userId, ZhongZhiChengChengBadger.id, "1")
        }
        userInfoService.updateRewards(userId, id, reposts)
      }
    }, 167, TimeUnit.HOURS)
    0
  }
}
