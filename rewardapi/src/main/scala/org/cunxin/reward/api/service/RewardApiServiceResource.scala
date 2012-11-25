package org.cunxin.reward.api.service

import javax.validation.Valid
import javax.ws.rs.{POST, Path, Produces}
import javax.ws.rs.core.MediaType
import com.google.inject.Inject
import com.yammer.metrics.annotation.Timed
import org.cunxin.reward.app.service.{UserInfoService, UserEventService}
import org.cunxin.reward.app.model.UserEventType

@Path("/rewardService")
@Produces(Array(MediaType.APPLICATION_JSON))
class RewardApiServiceResource @Inject()(userEventService: UserEventService,
                                         userInfoService: UserInfoService) {

  @POST
  @Timed
  @Path("/recordEvent")
  def recordEvent(@Valid req: CunxinRewardApiRequest): Option[CunxinRewardApiResponse] = {
    val result = userEventService.recordEvent(req.userId, req.projectId, req.eventType, req.params)
    Some(CunxinRewardApiResponse(result))
  }

  @POST
  @Timed
  @Path("/getPoints")
  def getPoints(@Valid req: CunxinRewardApiRequest): Option[CunxinRewardApiResponse] = {
    val result = userInfoService.getPoints(req.userId)
    Some(CunxinRewardApiResponse(Map("userId" -> req.userId, "points" -> result.toString)))
  }

  @POST
  @Timed
  @Path("/getBadgerIds")
  def getBadgerIds(@Valid req: CunxinRewardApiRequest): Option[CunxinRewardApiResponse] = {
    val result = userInfoService.getBadgerIds(req.userId)
    Some(CunxinRewardApiResponse(Map("userId" -> req.userId, "badgerIds" -> result.toString)))
  }

}

case class CunxinRewardApiRequest(userId: String, projectId: String, eventType: UserEventType, params: Map[String, List[String]])

case class CunxinRewardApiResponse(result: Map[String, String])