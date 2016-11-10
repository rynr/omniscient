package org.rjung.service.message;

import java.util.AbstractMap.SimpleEntry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Create and retrieve {@link Message}s.
 */
@RestController
public class MessageController {

    private static final String DEFAULT_PAGE = "1";
    private static final String DEFAULT_LIMIT = "100";

    @Autowired
    MessageService messages;

    /**
     * Build HEAD-request
     *
     * @param page
     *            Not used in HEAD-request.
     * @param limit
     *            Not used in HEAD-request.
     * @return {@link ResponseEntity} without a body.
     */
    @RequestMapping(value = "/messages", method = RequestMethod.HEAD)
    public ResponseEntity<Void> headMessages(
            @RequestParam(value = "page", required = false, defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(value = "limit", required = false, defaultValue = DEFAULT_LIMIT) int limit) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Retrieve paginated {@link Message}s.
     *
     * @param page
     *            Set the page to retrieve. The parameter can be omitted, the
     *            default is <tt>1</tt>.
     * @param limit
     *            Set the number of {@link Message}s to be retrieved. The
     *            parameter can be omitted, the default is <tt>100</tt>.
     * @return {@link ResponseEntity} without a list of {@link Message}s in
     *         text-representation.
     */
    @RequestMapping(value = "/messages", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getMessages(
            @RequestParam(value = "page", required = false, defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(value = "limit", required = false, defaultValue = DEFAULT_LIMIT) int limit) {
        return new ResponseEntity<>(
                messages.getMessages(page, limit).stream().map(Message::export).collect(Collectors.joining("\n")),
                HttpStatus.OK);
    }

    /**
     * Store a new {@link Message}.
     *
     * @param source
     *            The text-source of the message. See {@link MessageType} for
     *            more information on the format.
     * @param urlBuilder
     *            {@link UriComponentsBuilder} to be able to provide the
     *            <tt>location</tt>-header with the resource-URL of the new
     *            {@link Message}.
     * @return Status <tt>201</tt> and the resource-URL of the new Message in
     *         the <tt>location</tt>-header.
     */
    @RequestMapping(value = "/messages", method = RequestMethod.POST, consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Void> postMessage(@RequestBody String source, UriComponentsBuilder urlBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(urlBuilder.path("/messages/{id}").buildAndExpand(messages.save(source)).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/messages.html", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getMessagesHtml(
            @RequestParam(value = "page", required = false, defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(value = "limit", required = false, defaultValue = DEFAULT_LIMIT) int limit) {
        return new ModelAndView("messages", Stream.of(new SimpleEntry<>("messages", messages.getMessages(page, limit)))
                .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue)));
    }

    @RequestMapping(value = "/messages.html", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public RedirectView postMessageHtml(@RequestParam(name = "message") String source,
            UriComponentsBuilder urlBuilder) {
        messages.save(source);
        return new RedirectView(urlBuilder.path("/messages.html").toUriString());
    }
}
