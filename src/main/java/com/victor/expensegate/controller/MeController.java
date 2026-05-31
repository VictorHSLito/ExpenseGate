package com.victor.expensegate.controller;

import com.victor.expensegate.controller.dto.UserInfoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/me")
public class MeController {

    @GetMapping
    public ResponseEntity<UserInfoDTO> me(Authentication authentication) {

        var authorities = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).toList();

        UserInfoDTO userInfo = new UserInfoDTO(authentication.getName(), authorities);

        return ResponseEntity.ok(userInfo);
    }
}
