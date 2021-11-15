package com.meli.fede.markoo.proxy.view.response;

import com.meli.fede.markoo.proxy.view.data.model.RequestData;
import lombok.Data;

@Data
public class ComboInfoResponse extends BaseInfoResponse {
    private String combo;

    public static ComboInfoResponse from(final RequestData requestData) {
        final ComboInfoResponse comboInfoResponse = new ComboInfoResponse();
        comboInfoResponse.combo = requestData.getCombo();
        comboInfoResponse.deniedCant = requestData.getDeniedCant();
        comboInfoResponse.requestCant = requestData.getRequestedCant();
        return comboInfoResponse;
    }
}
