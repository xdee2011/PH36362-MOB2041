package com.xdee.jpmart.model;

public class NguoiDung {

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_EMPLOYEE = "EMPLOYEE";

    private String username;
    private String password;
    private String fullname;
    private String role;

    public NguoiDung() {
    }

    public NguoiDung(String username, String password, String fullname, String role) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEmployee() {
        return ROLE_EMPLOYEE.equals(role);
    }

    public boolean isAdmin() {
        return role == null || ROLE_ADMIN.equals(role);
    }
}
