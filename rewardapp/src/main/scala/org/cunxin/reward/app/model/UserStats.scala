package org.cunxin.reward.app.model

import org.codehaus.jackson.annotate.{JsonProperty, JsonIgnoreProperties}
import collection.mutable
import java.util.Date

trait UserStats {
    def userId: String
    def projectStats: mutable.HashMap[String, EventStats]
}

@JsonIgnoreProperties(ignoreUnknown = true)
case class EventStats(@JsonProperty("stats") stats: mutable.HashMap[UserEventType, Int])

@JsonIgnoreProperties(ignoreUnknown = true)
case class UserAllTimeStats(@JsonProperty("userId") userId: String,
                            @JsonProperty("projectStats") projectStats: mutable.HashMap[String, EventStats],
                            @JsonProperty("lastUpdateDate") lastUpdateDate: Date)

@JsonIgnoreProperties(ignoreUnknown = true)
case class UserDailyStats(@JsonProperty("userId") userId: String,
                          @JsonProperty("projectStats") projectStats: mutable.HashMap[String, EventStats],
                          @JsonProperty("date") date: Date)
