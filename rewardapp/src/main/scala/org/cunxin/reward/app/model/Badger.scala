package org.cunxin.reward.app.model

import org.codehaus.jackson.annotate.{JsonProperty, JsonIgnoreProperties}

@JsonIgnoreProperties(ignoreUnknown = true)
case class Badger(@JsonProperty("badgerId") id: String,
                  @JsonProperty("name") name: String,
                  @JsonProperty("rule") rule: Map[UserEventType, Int])
