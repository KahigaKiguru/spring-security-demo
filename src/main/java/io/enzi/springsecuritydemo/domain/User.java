package io.enzi.springsecuritydemo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "application_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("userId")
    @JsonIgnore
    private Long userId;

    @JsonProperty("username")
    private String userName;

    @JsonProperty("password")
    private String password;

    @JsonProperty("emailAddress")
    private String emailAddress;

    @JsonProperty("roles")
    @ManyToMany(fetch = FetchType.EAGER, targetEntity = Role.class)
    private Collection<Role> roles = new ArrayList<>();
}
