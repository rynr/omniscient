package org.rjung.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * The {@link OmniscientApplication} helps you to keep track of things that have
 * happened and that need to be taken care of.
 */
@EnableOAuth2Sso
@SpringBootApplication
public class OmniscientApplication extends WebSecurityConfigurerAdapter {

    protected OmniscientApplication() {
    }

    /**
     * Main entry-point.
     *
     * @param args
     *            execution parameters.
     */
    public static void main(final String[] args) {
        SpringApplication.run(OmniscientApplication.class, args);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .antMatcher("/**")
            .authorizeRequests()
                .antMatchers("/", "/css/**")
                .permitAll()
            .anyRequest()
                .authenticated()
            .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .permitAll()
            .and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        // @formatter:on
    }

}
