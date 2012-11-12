package org.cunxin.reward.app.model.reward

import org.cunxin.reward.app.model.{UserDailyStats, UserAllTimeStats}

trait Reward {
    def id: String
    def isQualify(allTimeStats: UserAllTimeStats, dailyStatsList: List[UserDailyStats]): Boolean
    def register()
}
