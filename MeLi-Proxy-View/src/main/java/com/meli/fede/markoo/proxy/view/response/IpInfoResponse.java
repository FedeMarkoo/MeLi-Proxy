package com.meli.fede.markoo.proxy.view.response;

import com.meli.fede.markoo.proxy.view.data.model.RequestData;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

@Data
public class IpInfoResponse extends BaseInfoResponse {
    private String ip;

    public static IpInfoResponse from(final RequestData requestData) {
        final IpInfoResponse ipInfoResponse = new IpInfoResponse();
        ipInfoResponse.ip = requestData.getIp();
        ipInfoResponse.deniedCant = requestData.getDeniedCant();
        ipInfoResponse.requestCant = requestData.getRequestedCant();
        return ipInfoResponse;
    }

    @Override
    public boolean equals(final Object obj) {
        return ObjectUtils.nullSafeEquals(this.ip, obj) || super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.ip);
    }
}
