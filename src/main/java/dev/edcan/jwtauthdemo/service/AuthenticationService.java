package dev.edcan.jwtauthdemo.service;

import dev.edcan.jwtauthdemo.model.AuthenticationResponse;
import dev.edcan.jwtauthdemo.model.User;
import dev.edcan.jwtauthdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {


  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;


  @Autowired
  public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;
  }


  public AuthenticationResponse register(User request) {

    if(userRepository.findByUsername(request.getUsername()).isPresent()) {
      return new AuthenticationResponse("EL USUARIO YA EXISTE");
    }

    User userToSave = new User();
    userToSave.setFirstName(request.getFirstName());
    userToSave.setLastName(request.getLastName());
    userToSave.setUsername(request.getUsername());
    userToSave.setPassword( passwordEncoder.encode(request.getPassword()));

    userToSave.setRole(request.getRole());

    User savedUser = userRepository.save(userToSave);

    String jwt = jwtService.generateToken(savedUser);

    return  new AuthenticationResponse(jwt);
  }

  public AuthenticationResponse authenticate(User request) {

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getUsername(),
            request.getPassword()
        )
    );

    User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
    String jwt = jwtService.generateToken(user);

    return new AuthenticationResponse(jwt);
  }


}
