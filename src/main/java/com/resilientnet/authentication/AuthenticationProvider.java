package com.resilientnet.authentication;


import com.resilientnet.model.User;
import com.resilientnet.utils.Json;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;


public class AuthenticationProvider {

    public static User authenticate (String oidcToken) throws Exception {
        //authenticator receives username and token to submit to IDP

        try{
            User idpValidated = validateFromIDP(oidcToken);
            User appAuthenticated = authenticateUser(idpValidated);
            if (!idpValidated.isValid()) {
                throw new Exception("IDP says: invalid token");
            }
            else if(!idpValidated.isAuthenticated()){
                throw new Exception("User cannot consume this APIs");
            }
            return appAuthenticated;
        }
        catch (Exception e) {
            throw new Exception(e);
        }
    }

    private static User authenticateUser(User idpValidated) {
        idpValidated.authenticate();
        return idpValidated;
    }


    private static User validateFromIDP(String auth) throws Exception{
        //get the authentication string and validate it to the IDP
        //username will be retrived
        URI uri = URI.create("https://idp.resilientnet.com/auth/realms/resilientnetidp/protocol/openid-connect/userinfo");


        //init an HTTP client
        HttpClient cl = HttpClient.newBuilder().build();
        HttpRequest req = HttpRequest.newBuilder().GET().setHeader("Authorization", auth).uri(uri).build();
        try{
            HttpResponse<String> res =  cl.send(req, HttpResponse.BodyHandlers.ofString());

            //if status from IDP is not OK token is invalid
            if(res.statusCode() != 200)
                return new User(false);

            //Token validate, retrieves the username
            Map<String, Object> map = Json.toMap(res.body());
            return new User(map, true);
        }
        catch (Exception e){
            throw new Exception("IDP_REQ_FAILED", e);
        }
    }
}