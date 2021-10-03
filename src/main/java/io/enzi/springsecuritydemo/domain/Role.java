package io.enzi.springsecuritydemo.domain;

import io.enzi.springsecuritydemo.common.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role {

    @Id
    private Long roleId;
    private RoleType role;
}
