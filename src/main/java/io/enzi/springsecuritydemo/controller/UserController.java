package io.enzi.springsecuritydemo.controller;

import io.enzi.springsecuritydemo.domain.Role;
import io.enzi.springsecuritydemo.domain.User;
import io.enzi.springsecuritydemo.requests.AddRoleToUserRequest;
import io.enzi.springsecuritydemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/getUser")
    public ResponseEntity<User> getUser(@PathVariable("userId") Long userId){
        return ResponseEntity.ok().body(userService.getUserById(userId));
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @PostMapping("/user/add")
    public ResponseEntity<User> addUser(@RequestBody User newUser) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/add").toUriString());
        return ResponseEntity.created(uri).body(userService.addUser(newUser));
    }
    @PostMapping("/role/add")
    public ResponseEntity<Role> addRole(@RequestBody Role newRole) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/add").toUriString());
        return ResponseEntity.created(uri).body(userService.addRole(newRole));
    }

    @PostMapping("/user/addRole")
    public void addUserRole(@RequestBody AddRoleToUserRequest request){

        userService.addUserRole(request);
    }
}
