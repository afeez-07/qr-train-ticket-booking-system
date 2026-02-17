package com.trainbooking.qr_train_ticket.controller;

import com.trainbooking.qr_train_ticket.model.User;
import com.trainbooking.qr_train_ticket.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    private final UserRepository repo;

    public AuthController(UserRepository repo){ this.repo=repo; }

    @PostMapping("/register")
    public String register(@RequestBody User user){
        repo.save(user);
        return "Registered";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user){
        return repo.findByUsername(user.getUsername())
                .filter(u->u.getPassword().equals(user.getPassword()))
                .map(u->"Login Success")
                .orElse("Invalid Credentials");
    }
}