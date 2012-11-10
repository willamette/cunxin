package org.cunxin.reward.api.service

import javax.ws.rs.{Path, POST, Produces}
import scala.Array
import javax.ws.rs.core.MediaType
import com.yammer.metrics.annotation.Timed
import org.cunxin.reward.app.service.{RewardService, UserService, UserRewardService, UserEventService}
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
    def recordEvent(@Valid req: CunxinRewardEventRequest): Option[RewardApiResponse] = {
        userEventService.recordEvent(req.userId, req.eventType)
        Some(RewardApiResponse(true, "Successful"))
    }

    @POST
    @Timed
    @Path("/getBadgerByUser")
    def getBadgerByUser(@Valid req: CunxinRewardBadgerRequest): Option[Badger] = userRewardService.getBadger(req.userId, req.badgerId)

    @POST
    @Timed
    @Path("/getAllBadgerByUser")
    def getAllBadgerByUser(@Valid userId: String): Option[Set[Badger]] = Option(userRewardService.getAllBadger(userId))

}

case class CunxinRewardEventRequest(userId: String, eventType: UserEventType)

case class CunxinRewardBadgerRequest(userId: String, badgerId: String)