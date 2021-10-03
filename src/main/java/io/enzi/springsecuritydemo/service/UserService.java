package io.enzi.springsecuritydemo.service;

import io.enzi.springsecuritydemo.common.RoleType;
import io.enzi.springsecuritydemo.domain.Role;
import io.enzi.springsecuritydemo.domain.User;
import io.enzi.springsecuritydemo.repository.UserRepository;
import io.enzi.springsecuritydemo.repository.RoleRepository;
import io.enzi.springsecuritydemo.requests.AddRoleToUserRequest;
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

    public User addUser(User newUser){
        return userRepository.save(newUser);
    }

    public void addUserRole(AddRoleToUserRequest request){
        User user = userRepository.findByUserName(request.getUsername()).get();
        Role role = roleRepository.findByRole(request.getRole()).get();

        user.getRoles().add(role);

    }

    public Role addRole(Role role){
        return roleRepository.save(role);
    }

    public User getUserById(Long userId){
        return userRepository.findById(userId).get();
    }

    public User getUserByUserName(String userName){
        return userRepository.findByUserName(userName).get();
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
}
