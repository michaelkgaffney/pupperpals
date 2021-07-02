package org.pupperpals.pupperpals;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;

@RestController
public class PupperPalsController {
    @GetMapping("/")
    @ResponseStatus(HttpStatus.FOUND)
    public void redirectToDocs(HttpServletResponse httpServletResponse){
        httpServletResponse.setHeader("Location", "/docs/index.html");
    }

}
