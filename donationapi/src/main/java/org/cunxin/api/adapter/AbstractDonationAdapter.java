package org.cunxin.api.adapter;

import org.cunxin.api.model.CunxinDonationRequest;
import org.cunxin.app.dao.CunxinDonationDao;
import org.cunxin.app.model.network.response.CunxinDonationResponse;
import org.cunxin.app.service.CunxinDonationService;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Path("/donation")
public abstract class AbstractDonationAdapter {

    protected CunxinDonationService _cunxinDonationService;
    protected CunxinDonationDao _cunxinDonationDao;

    protected AbstractDonationAdapter(CunxinDonationService cunxinDonationService, CunxinDonationDao cunxinDonationDao) {
        _cunxinDonationService = cunxinDonationService;
        _cunxinDonationDao = cunxinDonationDao;
    }

    protected abstract CunxinDonationResponse doProcess(CunxinDonationRequest request);

}
