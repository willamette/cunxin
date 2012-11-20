package org.cunxin.reward.app.model

import org.codehaus.jackson.annotate.{JsonProperty, JsonIgnoreProperties}
import collection.mutable
import java.util.Date

trait UserStats {
  def userId: String
  def projectStats: mutable.HashMap[String, EventStats]
  def allEventStats: EventStats
}

//TODO: Code Smell
@JsonIgnoreProperties(ignoreUnknown = true)
case class EventStats(@JsonProperty("stats") stats: mutable.HashMap[UserEventType, Int])

object EventStats {
  def getNewEventStats: EventStats = EventStats(new mutable.HashMap[UserEventType, Int]())
}

@JsonIgnoreProperties(ignoreUnknown = true)
case class UserAllTimeStats(@JsonProperty("userId") userId: String,
                            @JsonProperty("projectStats") projectStats: mutable.HashMap[String, EventStats],
                            @JsonProperty("allEventStats") allEventStats: EventStats,
                            @JsonProperty("lastUpdateDate") lastUpdateDate: Date) extends UserStats

object UserAllTimeStats {
  def getNewAllTimeStats(userId: String): UserAllTimeStats =
    UserAllTimeStats(
      userId,
      new mutable.HashMap[String, EventStats](),
      EventStats.getNewEventStats,
      new Date()
    )
}

@JsonIgnoreProperties(ignoreUnknown = true)
case class UserActivity(@JsonProperty("userId") userId: String,
                        @JsonProperty("projectId") projectId: String,
                        @JsonProperty("event") event: UserEventType,
                        @JsonProperty("data") data: Map[String, List[String]],
                        @JsonProperty("date") date: Date)
