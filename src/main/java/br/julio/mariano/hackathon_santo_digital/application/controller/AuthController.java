package br.julio.mariano.hackathon_santo_digital.application.controller;

import org.openapitools.api.AuthApi;
import org.openapitools.model.OkResponse;
import org.openapitools.model.SigninForm;
import org.openapitools.model.SignupForm;
import org.openapitools.model.UserData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.julio.mariano.hackathon_santo_digital.application.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class AuthController implements AuthApi {

  private final AuthenticationService authenticationService;

  @Override
  public ResponseEntity<UserData> _login(SigninForm signinForm) {
    UserData userData = authenticationService.authenticate(
        signinForm.getUsername(),
        signinForm.getPassword());
    return ResponseEntity.status(HttpStatus.OK).body(userData);
  }

  @Override
  public ResponseEntity<OkResponse> _signup(@Valid SignupForm signupForm) {
    authenticationService.registerUser(signupForm.getUsername(), signupForm.getPassword(), signupForm.getRole());
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new OkResponse().message("User [%s] registered successfully!".formatted(signupForm.getUsername())));
  }

}
