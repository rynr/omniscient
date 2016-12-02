package org.rjung.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

import org.apache.catalina.connector.CoyotePrincipal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

@RunWith(MockitoJUnitRunner.class)
public class RootControllerTest {

    @InjectMocks
    private RootController sut; // System Under Test

    @Test
    public void verifyAnonymousUserGetsRootView() {
        Object result = sut.getRoot(UriComponentsBuilder.fromHttpUrl("https://www.example.com/"), null);

        assertThat(result, instanceOf(ModelAndView.class));
        assertThat(((ModelAndView) result).getViewName(), is("root"));
    }

    @Test
    public void verifyAuthorizedUserGetsRedirected() {
        Object result = sut.getRoot(UriComponentsBuilder.fromHttpUrl("https://www.example.com/"),
                new CoyotePrincipal("name"));

        assertThat(result, instanceOf(RedirectView.class));
        assertThat(((RedirectView) result).getUrl(), is("https://www.example.com/messages.html"));
    }
}
