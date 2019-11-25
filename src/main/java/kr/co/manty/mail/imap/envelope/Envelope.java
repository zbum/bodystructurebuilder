package kr.co.manty.mail.imap.envelope;

/**
 * ENVELOPE content.
 */
public interface Envelope {

    /**
     * Gets the envelope <code>date</code>. This is the value of the RFC822
     * <code>date</code> header.
     *
     * @return envelope Date or null if this attribute is <code>NIL</code>
     */
    String getDate();

    /**
     * Gets the envelope <code>subject</code>. This is the value of the
     * RFC822 <code>subject</code> header.
     *
     * @return subject, or null if this attribute is <code>NIL</code>
     */
    String getSubject();

    /**
     * Gets the envelope <code>from</code> addresses.
     *
     * @return from addresses, not null
     */
    Address[] getFrom();

    /**
     * Gets the envelope <code>sender</code> addresses.
     *
     * @return <code>sender</code> addresses, not null
     */
    Address[] getSender();

    /**
     * Gets the envelope <code>reply-to</code> addresses.
     *
     * @return <code>reply-to</code>, not null
     */
    Address[] getReplyTo();

    /**
     * Gets the envelope <code>to</code> addresses.
     *
     * @return <code>to</code>, or null if <code>NIL</code>
     */
    Address[] getTo();

    /**
     * Gets the envelope <code>cc</code> addresses.
     *
     * @return <code>cc</code>, or null if <code>NIL</code>
     */
    Address[] getCc();

    /**
     * Gets the envelope <code>bcc</code> addresses.
     *
     * @return <code>bcc</code>, or null if <code>NIL</code>
     */
    Address[] getBcc();

    /**
     * Gets the envelope <code>in-reply-to</code>.
     *
     * @return <code>in-reply-to</code> or null if <code>NIL</code>
     */
    String getInReplyTo();

    /**
     * Gets the envelope <code>message
     *
     * @return the message id
     */
    String getMessageId();

    /**
     * Values an envelope address.
     */
    interface Address {

        /** Empty array */
        Address[] EMPTY = {};

        /**
         * Gets the personal name.
         *
         * @return personal name, or null if the personal name is
         *         <code>NIL</code>
         */
        String getPersonalName();

        /**
         * Gets the SMTP source route.
         *
         * @return SMTP at-domain-list, or null if the list if
         *         <code>NIL</code>
         */
        String getAtDomainList();

        /**
         * Gets the mailbox name.
         *
         * @return the mailbox name or the group name when
         *         {@link #getHostName()} is null
         */
        String getMailboxName();

        /**
         * Gets the host name.
         *
         * @return the host name, or null when this address marks the start
         *         or end of a group
         */
        String getHostName();
    }
}