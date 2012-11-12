package org.cunxin.reward.app.model.reward.points

import org.codehaus.jackson.annotate.{JsonProperty, JsonIgnoreProperties}
import org.cunxin.reward.app.model.reward.{RewardsIterator, Reward}
import com.google.inject.Inject

@JsonIgnoreProperties(ignoreUnknown = true)
abstract class Points(@JsonProperty("badgerId") id: String,
                      @JsonProperty("amount") amount: Int,
                      @JsonProperty("days") days: Int) extends Reward {
    @Inject
    def register() {
        RewardsIterator.registerPoints(this)
    }
}

