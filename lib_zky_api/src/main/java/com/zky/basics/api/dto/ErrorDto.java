package com.zky.basics.api.dto;

/**
 * Created by lk
 * Date 2019-11-13
 * Time 16:31
 * Detail:
 */
public class ErrorDto {
    public int code ;
    public String msg = "";

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
