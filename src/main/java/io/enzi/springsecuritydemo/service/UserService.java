package io.enzi.springsecuritydemo.service;

import io.enzi.springsecuritydemo.domain.Role;
import io.enzi.springsecuritydemo.domain.User;
import io.enzi.springsecuritydemo.repository.UserRepository;
import io.enzi.springsecuritydemo.repository.RoleRepository;
import io.enzi.springsecuritydemo.requests.AddRoleToUserRequest;
import io.enzi.springsecuritydemo.requests.AddUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public User addUser(AddUserRequest request){
        User user = new User();
        user.setUserId(null);
        user.setUserName(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmailAddress(request.getEmailAddress());
        user.setRoles(new ArrayList<>());
        return userRepository.save(user);
    }

    public void addUserRole(AddRoleToUserRequest request){
        User user = getUserByUserName(request.getUsername());
        Role role = roleRepository.findByRole(request.getRole()).get();

        user.getRoles().add(role);

    }

    public Role addRole(String roleName){

        Role role = new Role();
        role.setRoleId(null);
        role.setRole(roleName);

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

    public List<Role> getAllRoles(){
        return roleRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserByUserName(username);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (Role role : user.getRoles())
            authorities.add(new SimpleGrantedAuthority(role.getRole()));

        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), authorities);
    }
}
