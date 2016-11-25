package org.rjung.service.helper;

import java.math.BigInteger;
import java.security.Principal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.apache.catalina.connector.CoyotePrincipal;
import org.rjung.service.message.Message;
import org.rjung.service.message.MessageDTO;
import org.rjung.service.message.MessageType;

public class TestHelper {

    private static SecureRandom secureRandom;

    // pure static helper class cannot be instantiated.
    private TestHelper() {
    }

    /**
     * Retrieve one of some enums by random.
     *
     * @param enums
     *            The list of available enums.
     * @return One randomly selected enum.
     */
    public static <T> T randomEnum(T[] enums) {
        return enums[randomInt(enums.length)];
    }

    /**
     * Retrieve a random number from 0 (inclusive) to a maximal given value
     * (exclusive).
     *
     * @param max
     *            The maximal value (exclusive).
     * @return A random number from 0 (inclusive) to the maximal value
     *         (exclusive).
     */
    public static int randomInt(final int max) {
        return getRandom().nextInt(max);
    }

    /**
     * Retrieve a random long number.
     *
     * @return A random long number.
     */
    public static long randomLong() {
        return getRandom().nextLong();
    }

    /**
     * Retrieve a random {@link LocalDateTime}.
     *
     * @return A random {@link LocalDateTime}.
     */
    public static LocalDateTime randomTime() {
        return LocalDateTime.ofEpochSecond(randomInt(999999999), randomInt(999999999), ZoneOffset.UTC);
    }

    /**
     * Retrieve a random {@link MessageDTO}.
     *
     * @return A random {@link MessageDTO}.
     */
    public static MessageDTO randomMessageDTO() {
        return new MessageDTO(randomString(16), randomString(36), randomEnum(MessageType.values()).toString(),
                randomString(255), randomTime().toEpochSecond(ZoneOffset.UTC));
    }

    /**
     * Retrieve a random {@link Message}.
     *
     * @return A random {@link Message}.
     */
    public static Message randomMessage() {
        return new Message(randomEnum(MessageType.values()), randomString(255), randomTime());
    }

    /**
     * Retrieve a random {@link String} with the given length.
     *
     * @param length
     *            The length of the random string to be retrieved.
     * @return A random {@link String} with the given length.
     */
    public static String randomString(final int length) {
        return new BigInteger(5 * length, getRandom()).toString(32);
    }

    public static Principal randomPrincipal() {
        return new CoyotePrincipal(randomString(36));
    }

    private static SecureRandom getRandom() {
        if (secureRandom == null) {
            secureRandom = new SecureRandom();
        }
        return secureRandom;
    }

}
