package org.cunxin.reward.app.model

import org.codehaus.jackson.annotate.{JsonIgnoreProperties, JsonProperty}

@JsonIgnoreProperties(ignoreUnknown = true)
case class PointRule(@JsonProperty("id") id: String,
                     @JsonProperty("points") points: Int,
                     @JsonProperty("rule") rule: Map[UserEventType, Int])