package com.resilientnet.controllers;
import com.resilientnet.api.JwtTokenUtils;
import com.resilientnet.authentication.AuthenticationProvider;
import com.resilientnet.models.JwtResponse;
import com.resilientnet.models.User;
import com.resilientnet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Objects;


@RestController
public class AuthController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    public JwtTokenUtils jwtTokenUtils;

    @RequestMapping("/")
    public ResponseEntity<String> index() {
        String data = "Welcome to ResilientNet Interface (r-API)\n I can check your token and tell who you are at /basic/token";
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @RequestMapping(value = "/basic/authenticate", produces = "application/json")
    public ResponseEntity <?> generateAuthenticationToken(@RequestHeader("Authorization") String auth, HttpServletResponse response) throws Exception{
        User authenticated = authenticate(auth);
        pushOrCheckDb(authenticated);
        final String token = jwtTokenUtils.generateToken(authenticated);
        Cookie uid = new Cookie("_uid",  Base64.getEncoder().withoutPadding().encodeToString(authenticated.getSubject().getBytes()));
        response.addCookie(uid);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private User authenticate(String auth) throws Exception {
        Objects.requireNonNull(auth);
        try {
            return AuthenticationProvider.authenticate(auth);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    //once the user is authenticated verify if its his first access and create its instance into DB
    private void pushOrCheckDb(User u) throws Exception {
        try {
           // User _user = userRepository.findBySubject(u.getSubject());
            User _user = null;
            if (_user == null)
                //then create a new document into db
                try {
                    userRepository.save(u);
                } catch (Exception e) {
                    throw new Exception(e);
                }
        }catch (Exception e){
            throw new Exception(e);
        }
    }
}