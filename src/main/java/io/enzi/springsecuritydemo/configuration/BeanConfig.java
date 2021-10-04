package io.enzi.springsecuritydemo.configuration;

import io.enzi.springsecuritydemo.requests.AddRoleToUserRequest;
import io.enzi.springsecuritydemo.requests.AddUserRequest;
import io.enzi.springsecuritydemo.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class BeanConfig {

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner runner(UserService userService){
        return args -> {
         userService.addRole( "ROLE_USER");
         userService.addRole( "ROLE_MANAGER");
         userService.addRole( "ROLE_ADMIN");
         userService.addRole( "ROLE_SUPER_ADMIN");

         userService.addUser(new AddUserRequest("Kahiga", "123", "kahiga@gmail.com"));
         userService.addUser(new AddUserRequest("Mitchie", "123", "mitchie@gmail.com"));
         userService.addUser(new AddUserRequest("Muriithi", "123", "muriithi@gmail.com"));
         userService.addUser(new AddUserRequest("Njoki", "123", "njoki@gmail.com"));

         userService.addUserRole(new AddRoleToUserRequest("Kahiga", "ROLE_SUPER_ADMIN"));
         userService.addUserRole(new AddRoleToUserRequest("Muriithi", "ROLE_ADMIN"));
         userService.addUserRole(new AddRoleToUserRequest("Njoki", "ROLE_USER"));
         userService.addUserRole(new AddRoleToUserRequest("Mitchie", "ROLE_MANAGER"));

        };
    }
}
