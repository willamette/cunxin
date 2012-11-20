package org.cunxin.reward.app.model.reward.points

import com.google.inject.Inject
import org.cunxin.support.util.HttpDataStrategy
import org.cunxin.reward.app.model.{UserActivity, UserAllTimeStats, UserEventType}
import org.apache.http.client.methods.HttpGet
import org.cunxin.reward.app.service.UserInfoService

case class WeiBoResponse(id: String, comments: String, reposts: String)

class ShareToWeiBoPoints @Inject()(httpDataStrategy: HttpDataStrategy, userInfoService: UserInfoService) extends Points {
    def id = "shareToWeiBo"

    def observingEvents = List(UserEventType.WEIBO_SHARE)

    def onPublish(userId: String, projectId: String, eventType: UserEventType, data: Map[String, List[String]],
                  pastAllTimeStats: UserAllTimeStats, pastActivities: List[UserActivity]): Int = {
        if (pastAllTimeStats.projectStats.contains(projectId) && pastAllTimeStats.projectStats(projectId).stats.contains(UserEventType.WEIBO_SHARE))
            return 0
        val url = "http://api.weibo.com/2/statuses/count.json?ids=%s&access_token=%s".format(data("id")(0), data("token")(0))
        val result = httpDataStrategy.executeMethod[List[WeiBoResponse]](new HttpGet(url))
        result.getOrElse(return 0).head.reposts.toInt
        0
    }
}
