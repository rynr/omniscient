package org.rjung.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class RootController {

    @RequestMapping(value = "/")
    @ResponseStatus(value = HttpStatus.MOVED_PERMANENTLY)
    public RedirectView getRoot(final UriComponentsBuilder urlBuilder) {
        return new RedirectView(urlBuilder.path("/messages.html").toUriString());
    }
}
