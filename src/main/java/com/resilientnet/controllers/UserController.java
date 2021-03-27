package com.resilientnet.controllers;

import com.resilientnet.models.User;
import com.resilientnet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    /*
    * Get basic user infos. All the user informations are stored into the main mongoDB instance.
    * When first authn occurs, user info from idp are used to fill the db
    */
    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo() throws Exception {
        try {
            SecurityContext sc = SecurityContextHolder.getContext();
            Authentication auth = sc.getAuthentication();
            User _u = userRepository.findBySubject((String)auth.getPrincipal());
            return ResponseEntity.ok(_u);
        }catch (Exception e){
            throw new Exception(e);
        }
    }

    /*
    * User infos can be modified. No IDP data will be compromised/changed. Just local db is interested
    *
    */
    @PostMapping("/update")
    public ResponseEntity<Map<String, String>> updateUserInfo(){
        return null;
    }

}
