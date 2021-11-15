package com.meli.fede.markoo.proxy.data.redis.model;

import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash
public class Student implements Serializable {

    private static final long serialVersionUID = -1131833259052263211L;

    private String id;
    private String name;
    private int grade;
}