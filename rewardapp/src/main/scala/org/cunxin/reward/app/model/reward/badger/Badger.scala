package org.cunxin.reward.app.model.reward.badger

import org.codehaus.jackson.annotate.{JsonProperty, JsonIgnoreProperties}
import org.cunxin.reward.app.model.reward.{RewardsIterator, Reward}
import com.google.inject.Inject
import org.cunxin.reward.app.model.{UserDailyStats, UserAllTimeStats}

@JsonIgnoreProperties(ignoreUnknown = true)
abstract class Badger(@JsonProperty("badgerId") id: String,
                      @JsonProperty("name") name: String) extends Reward {
    @Inject
    def register() {
        RewardsIterator.registerBadger(this)
    }
}

class FirstBadger extends Badger {
    def id = null
    def isQualify(allTimeStats: UserAllTimeStats, dailyStatsList: List[UserDailyStats]) = false
}