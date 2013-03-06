package org.cunxin.reward.app.model.reward.points

import org.cunxin.reward.app.model.reward.Points
import org.cunxin.reward.app.model.{UserActivity, UserAllTimeStats, UserEventType}
import com.google.inject.Inject
import org.cunxin.reward.app.service.UserInfoService
import java.util.Date
import org.apache.commons.logging.LogFactory

class LoginPoints @Inject()(userInfoService: UserInfoService) extends Points {
  private[this] val logger = LogFactory.getLog(this.getClass)

  def id = "loginPoints"

  def observingEvents = List(UserEventType.LOGIN)

  def onPublish(userId: String, projectId: String, date: Date, eventType: UserEventType, data: Map[String, List[String]], pastAllTimeStats: UserAllTimeStats, pastActivities: List[UserActivity]): Int = {
    val (lastLoginDate, loginDays) = userInfoService.updateAndGetLoginDaysAndLastLoginDate(userId, date)

    val today = new Date(date.getYear, date.getMonth, date.getDate)
    val lastLoginDay = new Date(lastLoginDate.getYear, lastLoginDate.getMonth, lastLoginDate.getDate)
    if (today.getTime == lastLoginDay.getTime) {
      logger.info("User %s has logged in the same day".format(userId))
      return 0
    }
    //    "登陆网站，当天起每天获得1分
    //    连续登陆3天，则第3天起每天获得2分
    //    连续登陆5天，则第5天起每天获得3分
    //    连续登陆7天，则第7天起每天获得4分
    //    连续登陆15天，则第15天起每天获得5分
    //    连续登陆30天，则第30天起每天获得6分"
    val points =
      if (loginDays >= 30)
        6
      else if (loginDays >= 15)
        5
      else if (loginDays >= 7)
        4
      else if (loginDays >= 5)
        3
      else if (loginDays >= 3)
        2
      else 1
    logger.info("User %s has logged for %d continuous days, the logging bonus points will be %d".format(userId, loginDays, points))
    points
  }
}
