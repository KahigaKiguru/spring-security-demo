package io.enzi.springsecuritydemo.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AddRoleToUserRequest {

    private String username;

    private String role;

}
