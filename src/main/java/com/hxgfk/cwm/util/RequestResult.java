package com.hxgfk.cwm.util;

public class RequestResult {
    private int status = 403;
    private String body;

    public RequestResult(int status, String body) {
        this.body = body;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public String getData() {
        return body;
    }
}
