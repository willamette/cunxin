package org.cunxin.reward.app.dao

import com.google.inject.Inject
import com.mongodb.casbah.MongoDB
import org.cunxin.support.db.AbstractMongoDao
import org.cunxin.reward.app.model.UserEvent
import java.util.Date

class UserEventDao @Inject()(db: MongoDB) extends AbstractMongoDao[UserEvent](db, "userevent", version = 1) {

  def findAllEventsByUser(id: String, startDate: Option[Date], endDate: Option[Date]) =
    find("userId" -> id).filter(d => d.data.date.after(startDate.getOrElse(new Date(1970, 1, 1))) && d.data.date.before(new Date()))

  def findAllEventsByUser(id: String) = findAllEventsByUser(id, None, None)

}
