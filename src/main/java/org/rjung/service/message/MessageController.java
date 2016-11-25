package org.rjung.service.message;

import java.security.Principal;
import java.util.AbstractMap.SimpleEntry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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

    final MessageService messages;

    public MessageController(MessageService messages) {
        this.messages = messages;
    }

    /**
     * Build HEAD-request
     *
     * @return {@link ResponseEntity} without a body.
     */
    @RequestMapping(value = { "/messages", "/messages.html" }, method = RequestMethod.HEAD)
    public ResponseEntity<Void> headMessages() {
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
    @RequestMapping(value = "/messages.txt", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getMessagesText(
            @RequestParam(value = "page", required = false, defaultValue = DEFAULT_PAGE) final int page,
            @RequestParam(value = "limit", required = false, defaultValue = DEFAULT_LIMIT) final int limit,
            final Principal principal) {
        return new ResponseEntity<>(messages.getMessages(page, limit, principal).stream().map(Message::export)
                .collect(Collectors.joining("\n")), HttpStatus.OK);
    }

    /**
     * Retrieve {@link Message}s.
     *
     * @param id
     *            The <tt>id</tt> of the {@link Message}.
     * @param principal
     * @return {@link ResponseEntity} without a list of {@link Message}s in
     *         text-representation.
     */
    @RequestMapping(value = "/messages/{id:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}.txt", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getMessageText(@PathVariable(value = "id") final String id,
            final Principal principal) {
        return new ResponseEntity<>(messages.getMessage(id, principal).export(), HttpStatus.OK);
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
    @RequestMapping(value = "/messages.txt", method = RequestMethod.POST, consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Void> postMessageText(@RequestBody final String source, final UriComponentsBuilder urlBuilder,
            final Principal principal) {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(
                urlBuilder.path("/messages/{id}.txt").buildAndExpand(messages.save(source, principal)).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/messages.html", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getMessagesHtml(
            @RequestParam(value = "page", required = false, defaultValue = DEFAULT_PAGE) final int page,
            @RequestParam(value = "limit", required = false, defaultValue = DEFAULT_LIMIT) final int limit,
            final Principal principal) {
        return new ModelAndView("messages",
                Stream.of(new SimpleEntry<>("messages", messages.getMessages(page, limit, principal)))
                        .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue)));
    }

    @RequestMapping(value = "/messages.html", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public RedirectView postMessageHtml(@RequestParam(name = "message") final String source,
            final UriComponentsBuilder urlBuilder, final Principal principal) {
        messages.save(source, principal);
        return new RedirectView(urlBuilder.path("/messages.html").toUriString());
    }
}
