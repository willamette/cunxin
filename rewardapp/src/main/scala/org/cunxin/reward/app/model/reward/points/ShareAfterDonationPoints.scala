package org.cunxin.reward.app.model.reward.points

import collection.mutable
import org.cunxin.reward.app.model.{EventStats, UserActivity, UserAllTimeStats, UserEventType}

class ShareAfterDonationPoints extends Points {
    def id = "shareAfterDonationPoints"
    def observingEvents = List(UserEventType.DONATION_SHARE)
    def onPublish(userId: String, projectId: String, eventType: UserEventType, data: Map[String, List[String]],
                  pastAllTimeStats: UserAllTimeStats, pastActivities: List[UserActivity]): Int = {
        val projectStats = pastAllTimeStats.projectStats.getOrElse(projectId, EventStats(new mutable.HashMap[UserEventType, Int]())).stats.getOrElse(UserEventType.DONATION_SHARE, 0)
        if (projectStats == 0) 4 else 0
    }
}
