package org.cunxin.reward.app.model.reward.points

import com.google.inject.Inject
import org.cunxin.reward.app.model.reward.{ProcessChain, Reward}
import org.cunxin.reward.app.model.{EventStats, UserEventType, UserActivity, UserAllTimeStats}
import java.util.Date
import collection.mutable

abstract class Points extends Reward {
    @Inject
    def register() {
        ProcessChain.registerPoints(this)
    }
}

class DonationPoints extends Points {
    def id = "donationPoints"

    def observingEvents = List(UserEventType.DONATION)

    def publish(userId: String, projectId: String, eventType: UserEventType, data: Map[String, List[String]],
                pastAllTimeStats: UserAllTimeStats, pastActivities: List[UserActivity]): Int = {
        val basicPoints = {
            if (pastAllTimeStats.projectStats.contains(projectId) ||
                    pastAllTimeStats.projectStats(projectId).stats.contains(UserEventType.DONATION))
                0
            else
                1
        }
        val bonusPoints = data.getOrElse("amount", List("0")).head.toInt
        basicPoints + bonusPoints
    }
}

class ShareToWeiBoPoints extends Points {
    def id = "shareToWeiBo"
    def observingEvents = List(UserEventType.WEIBO_SHARE)
    def publish(userId: String, projectId: String, eventType: UserEventType, data: Map[String, List[String]],
                pastAllTimeStats: UserAllTimeStats, pastActivities: List[UserActivity]): Int = {
        0
    }
}

class ShareAfterDonationPoints extends Points {
    def id = "shareAfterDonationPoints"

    def observingEvents = List(UserEventType.DONATION_SHARE)

    def publish(userId: String, projectId: String, eventType: UserEventType, data: Map[String, List[String]],
                pastAllTimeStats: UserAllTimeStats, pastActivities: List[UserActivity]): Int = {
        val projectStats = pastAllTimeStats.projectStats.getOrElse(projectId, EventStats(new mutable.HashMap[UserEventType, Int]())).stats.getOrElse(UserEventType.DONATION_SHARE, 0)
        if (projectStats == 0) 4 else 0
    }
}

class SupportPoints extends Points {
    def id = "supportPoints"
    def observingEvents = List(UserEventType.SUPPORT)
    def publish(userId: String, projectId: String, eventType: UserEventType, data: Map[String, List[String]], pastAllTimeStats: UserAllTimeStats, pastActivities: List[UserActivity]): Int = {
        val today = new Date()
        today.setHours(0)
        today.setMinutes(0)
        today.setSeconds(0)
        val todayActivities = pastActivities.filter(a => a.date.compareTo(today) > -1 && a.event == UserEventType.SUPPORT)
        if (todayActivities.size >= 5) 0 else 1
    }
}