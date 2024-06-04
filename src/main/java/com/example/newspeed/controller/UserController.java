package com.example.newspeed.controller;
import com.example.newspeed.dto.SignUpRequestDto;
import com.example.newspeed.entity.User;
import com.example.newspeed.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignUpRequestDto signUpRequestDto){
        userService.singUp(signUpRequestDto);
        return new ResponseEntity<String>("회원가입 ", HttpStatus.OK);
    }
    
}
