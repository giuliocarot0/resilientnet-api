package com.resilientnet.authentication;


import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AuthenticationProvider {

    public static String authenticate (String oidcToken) throws Exception {
        //authenticator receives username and token to submit to IDP

        try{
            String username = validateFromIDP(oidcToken);
            if (username == null) {
                throw new Exception("IDP says: invalid token");
            }
            return username;
        }
        catch (Exception e) {
            throw new Exception(e);
        }
    }


    public static String validateFromIDP(String auth) throws Exception{
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
                return null;

            //Token validate, retrieves the username
            return getUserString(res.body());
        }
        catch (Exception e){
            throw new Exception("IDP_REQ_FAILED", e);
        }
    }

    private static String getUserString(String body){
        body = body.split(",")[3].split(":")[1];
        String user = body.substring(1, body.length()-1);
        return user;
    }
}