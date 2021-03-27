package com.resilientnet.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;
@Data
@Document(collection = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String _uid;
    private String subject, name, surname, email, birthday, gender, type, roles;
    private Boolean completed;
    @Transient
    private Boolean authenticated, valid;
    public User (String subject, String name, String surname, String email, Boolean valid){
        this.subject = subject;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.authenticated = false;
        this.valid = valid;
    }
    //constructor used by authentication controller to create a new user into db
    public User(User _u){
        this.subject = _u.subject;
        this.name = _u.name;
        this.surname = _u.surname;
        this.email = _u.email;
        this.birthday ="";
        this.gender= "";
        this.type="";
        this.roles="";
        this.completed = false;
    }
    //constructor used by authentication provider
    public User (Map<String,Object> idpUserData, Boolean valid){
        this.subject = (String)idpUserData.getOrDefault("preferred_username", null);
        this.name = (String)idpUserData.getOrDefault("given_name", null);
        this.surname =(String)idpUserData.getOrDefault("family_name", null);
        this.email = (String)idpUserData.getOrDefault("email", null);
        this.authenticated = false;
        this.valid = valid;
    }
    public User (Boolean valid){
        this.subject = null;
        this.name =  null;
        this.surname =null;
        this.email =  null;
        this.authenticated = false;
        this.valid = valid;
    }
    public String getSubject() {
        return subject;
    }
    public Boolean isValid(){
        return valid;
    }
    public void setSubject(String subject) {
        this.subject = subject;
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
