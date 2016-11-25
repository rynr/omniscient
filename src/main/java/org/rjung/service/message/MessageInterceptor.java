package org.rjung.service.message;

@FunctionalInterface
public interface MessageInterceptor {

    public void saveMessage(final MessageDTO message);
}
