package org.cunxin.reward.app.model

import java.util.Date
import org.codehaus.jackson.annotate.{JsonProperty, JsonIgnoreProperties}
import collection.mutable

@JsonIgnoreProperties(ignoreUnknown = true)
case class UserBadger(@JsonProperty("userId") userId: String,
                      @JsonProperty("badgerId") badgerId: String,
                      @JsonProperty("stats") stats: mutable.HashMap[UserEventType, Int],
                      @JsonProperty("rule") rule: Map[UserEventType, Int],
                      //For simplicity, this one is unused....
                      @JsonProperty("lastUpdateTime") lastUpdateTime: Date)
