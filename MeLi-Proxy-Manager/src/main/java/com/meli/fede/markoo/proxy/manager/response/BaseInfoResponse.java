package com.meli.fede.markoo.proxy.manager.response;

import lombok.Data;

@Data
public abstract class BaseInfoResponse {
    Long deniedCant;
    Long requestCant;
}
