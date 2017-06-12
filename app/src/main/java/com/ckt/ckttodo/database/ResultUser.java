package com.ckt.ckttodo.database;

/**
 * Created by ckt on 6/9/17.
 */

public class ResultUser {

    private int resultcode;
    private PostUser data;
    private String token;

    public int getResultcode() {
        return resultcode;
    }

    public void setResultcode(int resultcode) {
        this.resultcode = resultcode;
    }

    public PostUser getData() {
        return data;
    }

    public void setData(PostUser data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
