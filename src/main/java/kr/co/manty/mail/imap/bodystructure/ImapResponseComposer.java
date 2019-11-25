package kr.co.manty.mail.imap.bodystructure;

import java.io.IOException;

public interface ImapResponseComposer {
    /**
     * Composes a <code>NIL</code>.
     * 
     * @throws IOException
     */
    ImapResponseComposer nil() throws IOException;

    /**
     * Write the message of type <code>String</code>
     * 
     * @param message
     * @return composer
     * @throws IOException
     */
    ImapResponseComposer message(String message) throws IOException;

    /**
     * Write the message of type <code>Long</code>
     * 
     * @param number
     * @return composer
     * @throws IOException
     */
    ImapResponseComposer message(long number) throws IOException;

    /**
     * Write a CRLF and flush the composer which will write the content of it to
     * the socket
     * 
     * @return composer
     * @throws IOException
     */
    ImapResponseComposer end() throws IOException;

    /**
     * Write a quoted message
     * 
     * @param message
     * @return composer
     * @throws IOException
     */
    ImapResponseComposer quote(String message) throws IOException;

    /**
     * Write a '('
     * 
     * @return composer
     * @throws IOException
     */
    ImapResponseComposer openParen() throws IOException;

    /**
     * Write a ')'
     * 
     * @return composer
     * @throws IOException
     */
    ImapResponseComposer closeParen() throws IOException;

    /**
     * Appends the given message after conversion to upper case. The message may
     * be assumed to be ASCII encoded. Conversion of characters MUST NOT be
     * performed according to the current locale but as per ASCII.
     * 
     * @param message
     *            ASCII encoded, not null
     * @return self, not null
     * @throws IOException
     */
    ImapResponseComposer quoteUpperCaseAscii(String message) throws IOException;

    /**
     * Tell the {@link ImapResponseComposer} to skip the next written space
     * 
     * @return composer
     * @throws IOException
     */
    ImapResponseComposer skipNextSpace() throws IOException;


}
