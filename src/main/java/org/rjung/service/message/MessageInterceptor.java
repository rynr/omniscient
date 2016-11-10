package org.rjung.service.message;

@FunctionalInterface
public interface MessageInterceptor {

    public void saveMessage(MessageDTO message);
}
