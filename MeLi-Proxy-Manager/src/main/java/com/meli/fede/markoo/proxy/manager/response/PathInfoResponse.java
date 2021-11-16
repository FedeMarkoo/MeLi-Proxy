package com.meli.fede.markoo.proxy.manager.response;

import com.meli.fede.markoo.proxy.manager.data.model.RequestData;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

@Data
public class PathInfoResponse extends BaseInfoResponse {
    private String path;

    public static PathInfoResponse from(final RequestData requestData) {
        final PathInfoResponse pathInfoResponse = new PathInfoResponse();
        pathInfoResponse.path = requestData.getPath();
        pathInfoResponse.deniedCant = requestData.getDeniedCant();
        pathInfoResponse.requestCant = requestData.getRequestedCant();
        return pathInfoResponse;
    }

    @Override
    public boolean equals(final Object obj) {
        return ObjectUtils.nullSafeEquals(this.path, obj) || super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.path);
    }
}
