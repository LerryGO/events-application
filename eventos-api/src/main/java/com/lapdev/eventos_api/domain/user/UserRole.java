package com.lapdev.eventos_api.domain.user;

public enum UserRole {
    ADMIN("admin"),
    USEER("user");

    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole(){
        return  role;
    }
}
