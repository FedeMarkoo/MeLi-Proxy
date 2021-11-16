package com.meli.fede.markoo.proxy.manager.response;

import com.meli.fede.markoo.proxy.manager.data.model.RequestData;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

@Data
public class UserAgentInfoResponse extends BaseInfoResponse {

    private String userAgent;

    public static UserAgentInfoResponse from(final RequestData requestData) {
        final UserAgentInfoResponse userAgentInfoResponse = new UserAgentInfoResponse();
        userAgentInfoResponse.userAgent = requestData.getUserAgent();
        userAgentInfoResponse.deniedCant = requestData.getDeniedCant();
        userAgentInfoResponse.requestCant = requestData.getRequestedCant();
        return userAgentInfoResponse;
    }

    @Override
    public boolean equals(final Object obj) {
        return ObjectUtils.nullSafeEquals(this.userAgent, obj) || super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.userAgent);
    }
}
