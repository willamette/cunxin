package org.cunxin.reward.app.model

import org.codehaus.jackson.annotate.{JsonProperty, JsonIgnoreProperties}
import collection.mutable
import java.util.Date

@JsonIgnoreProperties(ignoreUnknown = true)
case class UserDailyStats(@JsonProperty("userId") userId: String,
                          @JsonProperty("stats") stats: mutable.HashMap[UserEventType, Int],
                          @JsonProperty("date") date: Date)
