package org.rjung.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The {@link OmniscientApplication} helps you to keep track of things that have
 * happened and that need to be taken care of.
 */
@SpringBootApplication
public class OmniscientApplication {

    protected OmniscientApplication() {
    }

    /**
     * Main entry-point.
     *
     * @param args
     *            execution parameters.
     */
    public static void main(String[] args) {
        SpringApplication.run(OmniscientApplication.class, args);
    }

}
