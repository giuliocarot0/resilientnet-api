package com.resilientnet.model;

import java.util.Map;

public class User {
    private String id, name, surname, email;
    private Boolean authenticated, valid;
    public User (String id, String name, String surname, String email, Boolean valid){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.authenticated = false;
        this.valid = valid;
    }
    public User (Map<String,Object> idpUserData, Boolean valid){
        this.id = (String)idpUserData.getOrDefault("preferred_username", null);
        this.name = (String)idpUserData.getOrDefault("given_name", null);
        this.surname =(String)idpUserData.getOrDefault("family_name", null);
        this.email = (String)idpUserData.getOrDefault("email", null);
        this.authenticated = false;
        this.valid = valid;
    }
    public User (Boolean valid){
        this.id = null;
        this.name =  null;
        this.surname =null;
        this.email =  null;
        this.authenticated = false;
        this.valid = valid;
    }
    public String getSubject() {
        return id;
    }
    public Boolean isValid(){
        return valid;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public Boolean isAuthenticated(){
        return this.authenticated;
    }


    public void authenticate() {
        this.authenticated = true;
    }
}
