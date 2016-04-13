package com.adrian.hackmyphone.items;

/**
 * Created by Adrian on 2016-04-13.
 */
public class SmsMsg {
    private String address;
    private String content;

    public SmsMsg() {
    }

    public SmsMsg(String address, String content) {
        this.address = address;
        this.content = content;
    }

    public String getAddress() {
        return address;
    }

    public String getContent() {
        return content;
    }
}
