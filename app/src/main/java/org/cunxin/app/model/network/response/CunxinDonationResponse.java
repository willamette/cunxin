package org.cunxin.app.model.network.response;

import org.cunxin.app.service.CunxinDonationServiceError;

import java.util.List;

public class CunxinDonationResponse {
    private boolean _hasErrors;
    private String _responseMessage;
    private List<CunxinDonationServiceError> _errors;

    public CunxinDonationResponse(boolean hasErrors, String responseMessage, List<CunxinDonationServiceError> errors) {
        _hasErrors = hasErrors;
        _responseMessage = responseMessage;
        _errors = errors;
    }

    public boolean isHasErrors() {
        return _hasErrors;
    }

    public String getResponseMessage() {
        return _responseMessage;
    }

    public List<CunxinDonationServiceError> getErrors() {
        return _errors;
    }
}
