package io.enzi.springsecuritydemo.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.enzi.springsecuritydemo.domain.Role;
import io.enzi.springsecuritydemo.domain.User;
import io.enzi.springsecuritydemo.requests.AddRoleToUserRequest;
import io.enzi.springsecuritydemo.requests.AddUserRequest;
import io.enzi.springsecuritydemo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
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

    @GetMapping("/getAllRoles")
    public ResponseEntity<List<Role>> getAllRoles(){
        return ResponseEntity.ok().body(userService.getAllRoles());
    }

    @PostMapping("/user/add")
    public ResponseEntity<User> addUser(@RequestBody AddUserRequest request) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/add").toUriString());
        return ResponseEntity.created(uri).body(userService.addUser(request));
    }
    @PostMapping("/role/add")
    public ResponseEntity<Role> addRole(@RequestBody String newRole) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/add").toUriString());
        return ResponseEntity.created(uri).body(userService.addRole(newRole));
    }

    @PostMapping("/user/addRole")
    public ResponseEntity<?> addUserRole(@RequestBody AddRoleToUserRequest request){
        userService.addUserRole(request);
        return ResponseEntity.ok().build();
    }
    @SneakyThrows
    @GetMapping("/refreshToken")
    public void
    refreshToken(HttpServletRequest request, HttpServletResponse response){
        String authorizationHeader = request.getHeader(AUTHORIZATION);

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
                try {
                    String token = authorizationHeader.substring("Bearer ".length());
                    Algorithm algorithm = Algorithm.HMAC256("secret".getBytes(StandardCharsets.UTF_8));
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(token);
                    String username = decodedJWT.getToken();

                    User user = userService.getUserByUserName(username);


                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

                    stream(roles).forEach(role -> {
                        authorities.add(new SimpleGrantedAuthority(role));
                    });

                    UsernamePasswordAuthenticationToken authenticationToken = new
                            UsernamePasswordAuthenticationToken(username, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                }catch (Exception e){
                    log.error("Error Logging in :: {}", e.getMessage());
                    response.setHeader("error", e.getMessage());
                    response.setStatus(FORBIDDEN.value());
//                    response.sendError(FORBIDDEN.value());

                    Map<String, String> error  = new HashMap<>();

                    error.put("error_message", e.getMessage());

                    response.setContentType(APPLICATION_JSON_VALUE);

                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }

            }else
                throw new RuntimeException("Refresh Token is missing");
        }
    }
}
