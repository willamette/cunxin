package org.cunxin.reward.app.model

import org.codehaus.jackson.annotate.{JsonProperty, JsonIgnoreProperties}
import scala.collection.mutable

trait Reward {
    def id: String
    def rule: Map[String, EventStats]
    //TODO: So ugly, code smell!!
    def qualifyRule(stats: mutable.Map[String, EventStats]): Boolean = {
        if (!stats.keySet.equals(rule.keySet)) return false
        stats.foreach {
            case (k, v) =>
                if (!v.stats.keySet.equals(rule(k).stats.keySet))
                    return false
                v.stats.foreach(kv => if (kv._2 < rule(k).stats(kv._1)) return false)
        }
        true
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
case class Badger(@JsonProperty("badgerId") id: String,
                  @JsonProperty("name") name: String,
                  //Activities are associated with Project, global Project Id is -1
                  @JsonProperty("rule") rule: Map[String, EventStats]) extends Reward {

}

@JsonIgnoreProperties(ignoreUnknown = true)
case class Points(@JsonProperty("badgerId") id: String,
                  @JsonProperty("amount") amount: Int,
                  //Activities are associated with Project, global Project Id is -1
                  @JsonProperty("rule") rule: Map[String, EventStats],
                  //-1 means all the time
                  @JsonProperty("days") days: Int) extends Reward {
}

