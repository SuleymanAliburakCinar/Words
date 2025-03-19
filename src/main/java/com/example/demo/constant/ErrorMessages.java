package com.example.demo.constant;

public enum ErrorMessages {

    ENTITY_NOT_FOUND("Entity not found."),
    ENTITY_ALREADY_EXIST("Entity already exist.");

    private final String msg;

    ErrorMessages(String msg){
        this.msg = msg;
    }

    public String getMessage(){
        return msg;
    }
}
