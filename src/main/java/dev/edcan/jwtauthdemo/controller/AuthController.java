package dev.edcan.jwtauthdemo.controller;


import dev.edcan.jwtauthdemo.model.AuthenticationResponse;
import dev.edcan.jwtauthdemo.model.User;
import dev.edcan.jwtauthdemo.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

  Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

  AuthenticationService authenticationService;


  @Autowired
  public AuthController(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @PostMapping("register")
  public ResponseEntity<AuthenticationResponse> register(
      @RequestBody User user
  ) {

    LOGGER.info("REGISTERING USER, {}", user.getUsername());

    return ResponseEntity.ok(authenticationService.register(user));

  }

  @PostMapping("login")
  public ResponseEntity<AuthenticationResponse> login(
      @RequestBody User user
  ) {

    LOGGER.info("LOGIN USER, {}", user.getUsername());
    return ResponseEntity.ok(authenticationService.authenticate(user));

  }
}
