package com.meli.fede.markoo.proxy.data.mongo.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
public class RequestData {
    private String ip;
    private String path;
    private String userAgent;
    private Long requestedCant;
    private Long deniedCant;
}
