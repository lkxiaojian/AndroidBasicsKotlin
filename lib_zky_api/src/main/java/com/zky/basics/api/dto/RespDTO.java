package com.zky.basics.api.dto;

import java.io.Serializable;


public class RespDTO<Any> implements Serializable{

    public int code ;
    public String msg = "";
    public Any data;

    @Override
    public String toString() {
        return "RespDTO{" +
                "code=" + code +
                ", error='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setError(String error) {
        this.msg = error;
    }
}
