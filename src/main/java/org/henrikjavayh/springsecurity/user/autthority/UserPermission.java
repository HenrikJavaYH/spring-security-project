package org.henrikjavayh.springsecurity.user.autthority;

public enum UserPermission {

    READ("READ"),
    WRITE("WRITE"),
    DELETE("DELETE")

    ;
    private String userPermission;

    UserPermission(String userPermission) {
        this.userPermission = userPermission;
    }
    public String getUserPermission() {
        return userPermission;
    }

}
