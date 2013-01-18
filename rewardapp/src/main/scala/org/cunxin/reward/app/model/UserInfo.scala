package org.cunxin.reward.app.model

import org.codehaus.jackson.annotate.{JsonProperty, JsonIgnoreProperties}
import java.util.Date

@JsonIgnoreProperties(ignoreUnknown = true)
case class UserInfo(@JsonProperty("userId") id: String,
                    @JsonProperty("points") points: Int,
                    @JsonProperty("lastLoginDate") lastLoginDate: Date,
                    @JsonProperty("loginDays") loginDays: Int,
                    @JsonProperty("receivedBadgerIds") receivedBadgerIds: Set[String],
                    @JsonProperty("version") version: Int = 1)
