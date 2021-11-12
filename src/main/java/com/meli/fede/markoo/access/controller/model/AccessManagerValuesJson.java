package com.meli.fede.markoo.access.controller.model;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;


@Getter
@Setter
public class AccessManagerValuesJson {
    @NotNull
    private Integer maxRequestPerIp;

    @NotNull
    private Integer maxRequestPerPath;

    @NotNull
    private Integer maxRequestPerCombo;
}
