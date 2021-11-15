package com.meli.fede.markoo.proxy.view.data.model;

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

    public long getDeniedCant() {
        return this.deniedCant == null ? 0 : this.deniedCant;
    }

    public String getCombo() {
        return this.ip.concat(" - ").concat(this.path);
    }
}
