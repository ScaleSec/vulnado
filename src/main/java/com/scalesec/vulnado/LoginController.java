package com.scalesec.vulnado;

import org.springframework.boot.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.boot.autoconfigure.*;

import java.io.Serializable;

@RestController
@EnableAutoConfiguration
public class LoginController {
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    LoginResponse login(@RequestBody LoginRequest input) {
        User user = User.fetch(input.username);
        if (Postgres.md5(input.password).equals(user.hashedPassword)) {
            return new LoginResponse("Access Granted! Have a cookie 🍪");
        } else {
            throw new Unauthorized("Access Denied");
        }
    }
}

class LoginRequest implements Serializable {
    public String username;
    public String password;
}

class LoginResponse implements Serializable {
    public String message;
    public LoginResponse(String msg) { this.message = msg; }
}

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class Unauthorized extends RuntimeException {

    public Unauthorized(String exception) {
        super(exception);
    }

}
