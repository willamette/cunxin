package org.cunxin.reward.app.model

import org.codehaus.jackson.annotate.{JsonProperty, JsonIgnoreProperties}
import java.util.Date

@JsonIgnoreProperties(ignoreUnknown = true)
case class UserEvent(@JsonProperty("userId") userId: String,
                     @JsonProperty("eventType") eventType: UserEventType,
                     @JsonProperty("date") date: Date)
