package kr.co.manty.mail.imap;

import javax.mail.MessagingException;

/**
 * A header.
 */
public interface Header extends Content {

    /**
     * Gets the name of this header.
     *
     * @return name of this header
     * @throws MessagingException
     */
    String getName();

    /**
     * Gets the (unparsed) value of this header.
     *
     * @return value of this header
     * @throws MessagingException
     */
    String getValue();
}
