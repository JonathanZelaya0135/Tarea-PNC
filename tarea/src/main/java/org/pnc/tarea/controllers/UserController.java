package org.pnc.tarea.controllers;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.pnc.tarea.entities.Post;
import org.pnc.tarea.entities.User;
import org.pnc.tarea.entities.dto.RegisterDto;
import org.pnc.tarea.entities.dto.UserDto;
import org.pnc.tarea.entities.dto.UserPostsDto;
import org.pnc.tarea.security.JwtUtil;
import org.pnc.tarea.services.PostService;
import org.pnc.tarea.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    private Pattern UUID_REGEX =
            Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@RequestBody @Valid RegisterDto info){
        try {
            userService.createUser(info);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") String id){
        try {
            return ResponseEntity.ok(userService.getUser(id));
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("users/email")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam("email") String email){
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/users/{id}/posts")
    public ResponseEntity<List<UserPostsDto>> getUserPosts(@PathVariable("id") String id){
        if(id == null || id.isEmpty() || !UUID_REGEX.matcher(id).matches()){
            return ResponseEntity.badRequest().build();
        }
        UUID userId = UUID.fromString(id);
        return ResponseEntity.ok(postService.getUserPosts(userId));
    }

    @GetMapping("/users/likes")
    public ResponseEntity<List<Post>> getLikedPosts(@RequestParam("userId") String id){
        if(id == null || id.isEmpty() || !UUID_REGEX.matcher(id).matches()){
            return ResponseEntity.badRequest().build();
        }
        UUID userId = UUID.fromString(id);
        return ResponseEntity.ok(postService.getLikedPosts(userId));
    }



}
