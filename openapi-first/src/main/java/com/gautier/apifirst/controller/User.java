package com.gautier.apifirst.controller;

import com.gautier.apifirst.api.UsersApi;
import com.gautier.apifirst.model.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class User implements UsersApi {

    @Override
    public ResponseEntity<UserDto> createUser(UserDto userDto) {
        UserDto user = new UserDto();
        user.setId("generated-id-" + UUID.randomUUID());
        user.setName(userDto.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

}
