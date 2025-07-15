package com.pm.authservice.contoller;

import com.pm.authservice.model.Users;
import com.pm.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    UserRepository repo;


    @GetMapping("/findMongoDocById")
    @CrossOrigin
    public Users getUserById() {
       Optional<Users> user=repo.findById("223e4567-e89b-12d3-a456-426614174006");
        // or handle the case where the user is not found
        return user.orElse(null);
    }

}