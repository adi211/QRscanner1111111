package com.example.adi.qrscanner;

public class Request {
    private String id;
    private String email;
    private boolean type;

    public Request(String id, String email, boolean type) {
        this.id = id;
        this.email = email;
        this.type = type;
    }

    public Request() {
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public boolean isType() {
        return type;
    }
}
