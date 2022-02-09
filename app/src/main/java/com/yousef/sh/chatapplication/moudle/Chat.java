package com.yousef.sh.chatapplication.moudle;

import java.util.HashMap;
import java.util.Map;

public class Chat {
    String sender;
    String reciver;
    String msg;
    String date;

    public Chat(String sender, String reciver, String msg, String date) {
        this.sender = sender;
        this.reciver = reciver;
        this.msg = msg;
        this.date = date;
    }

    public Chat() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
