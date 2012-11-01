package org.cunxin.reward.api.service

import javax.ws.rs.{Path, POST, Produces}
import scala.Array
import javax.ws.rs.core.MediaType
import com.yammer.metrics.annotation.Timed
import org.cunxin.reward.app.service.{BadgerService, UserService, UserRewardService, UserEventService}
import com.google.inject.Inject
import javax.validation.Valid
import org.cunxin.reward.api.model.RewardApiResponse
import org.cunxin.reward.app.model.{UserEventType, Badger}

@Path("/curationService")
@Produces(Array(MediaType.APPLICATION_JSON))
class RewardApiServiceResource @Inject()(userService: UserService,
                                         badgerService: BadgerService,
                                         userEventService: UserEventService,
                                         userRewardService: UserRewardService) {
  @POST
  @Timed
  @Path("/registerUserId")
  def registerUserId(@Valid userId: String): Option[RewardApiResponse] = {
    userService.createNewUser(userId)
    Some(RewardApiResponse(true, "Successful"))
  }

  @POST
  @Timed
  @Path("/registerBadger")
  def registerBadger(@Valid badger: Badger): Option[RewardApiResponse] = {
    badgerService.createBadger(badger)
    Some(RewardApiResponse(true, "Successful"))
  }

  @POST
  @Timed
  @Path("/recordEvent")
  def recordEvent(@Valid userId: String, @Valid eventType: UserEventType): Option[RewardApiResponse] = {
    userEventService.recordEvent(userId, eventType)
    Some(RewardApiResponse(true, "Successful"))
  }

  @POST
  @Timed
  @Path("/recordEvent")
  def getBadgerByUser(@Valid userId: String, badgerId: String): Option[Badger] = userRewardService.getBadger(userId, badgerId)

  @POST
  @Timed
  @Path("/recordEvent")
  def getAllBadgerByUser(@Valid userId: String): Option[Set[Badger]] = Option(userRewardService.getAllBadger(userId))

}
