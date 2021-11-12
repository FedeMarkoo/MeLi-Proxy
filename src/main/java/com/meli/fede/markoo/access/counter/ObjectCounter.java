package com.meli.fede.markoo.access.counter;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
class ObjectCounter {
    private String ip;
    private String path;
}
