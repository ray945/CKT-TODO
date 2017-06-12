package com.ckt.ckttodo.database;

import java.util.List;

/**
 * Created by ckt on 6/8/17.
 */

public class Result<T> {

    private int resultcode;
    private List<T> data;

    public int getResultcode() {
        return resultcode;
    }

    public void setResultcode(int resultcode) {
        this.resultcode = resultcode;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
