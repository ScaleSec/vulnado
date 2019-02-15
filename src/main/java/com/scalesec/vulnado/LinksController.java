package com.scalesec.vulnado;

import org.springframework.boot.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.boot.autoconfigure.*;
import java.util.List;
import java.io.Serializable;

@RestController
@EnableAutoConfiguration
public class LinksController {
    @RequestMapping(value = "/links", produces = "application/json")
    List<String> links(@RequestParam String url) {
        return LinkLister.getLinks(url);
    }
}
