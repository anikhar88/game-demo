package com.example.api.models;

import java.util.Objects;

public class ResponseBody {

    private String msg;
    private InnerBody body;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public InnerBody getBody() {
        return body;
    }

    public void setBody(InnerBody body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResponseBody that = (ResponseBody) o;
        return Objects.equals(msg, that.msg) &&
                Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(msg, body);
    }

    @Override
    public String toString() {
        return "ResponseBody{" +
                "msg='" + msg + '\'' +
                ", body=" + body +
                '}';
    }
}
