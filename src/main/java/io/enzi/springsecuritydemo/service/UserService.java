package io.enzi.springsecuritydemo.service;

import io.enzi.springsecuritydemo.common.RoleType;
import io.enzi.springsecuritydemo.domain.Role;
import io.enzi.springsecuritydemo.domain.User;
import io.enzi.springsecuritydemo.repository.UserRepository;
import io.enzi.springsecuritydemo.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    User addUser(User newUser){
        return userRepository.save(newUser);
    }

    void addUserRole(Long userId, RoleType roleType){
        User user = userRepository.findById(userId).get();
        Role role = roleRepository.findByRoleType(roleType).get();

        user.getRoles().add(role);

    }

    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    User getUser(Long userId){
        return userRepository.findById(userId).get();
    }
}
