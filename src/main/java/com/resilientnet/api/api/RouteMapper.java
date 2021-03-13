package com.resilientnet.api.api;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;


@RestController
public class RouteMapper {

    @RequestMapping("/")
    public ResponseEntity<String> index() {
        String data = "Welcome to ResilientNet Interface (r-API)\n I can check your token and tell who you are at /basic/token";
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @RequestMapping(value = "/basic/token", produces = "application/json")
    public ResponseEntity<Map<String, String>> auth(@RequestHeader("Authorization") String auth) throws IOException, InterruptedException {
        Map<String, String> data = new HashMap<>();
        String type = auth.split(" ")[0];
        String token = auth.split(" ")[1];
        data.put("type", type);
        data.put("token", token);

        URI uri = URI.create("https://idp.resilientnet.com/auth/realms/resilientnetidp/protocol/openid-connect/userinfo");


        //init an HTTP client
        HttpClient cl = HttpClient.newBuilder().build();
        HttpRequest req = HttpRequest.newBuilder().GET().setHeader("Authorization", auth).uri(uri).build();
        HttpResponse<String> res =  cl.send(req, HttpResponse.BodyHandlers.ofString());

        if(res.statusCode() != 200)
            return new ResponseEntity<>(data, HttpStatus.UNAUTHORIZED);
        data.put("user", res.body());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}