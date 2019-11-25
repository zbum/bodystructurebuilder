package kr.co.manty.mail.imap.bodystructure;

import java.io.IOException;

/**
 * <p>
 * Writes IMAP response.
 * </p>
 * <p>
 * Factors out basic IMAP response writing operations from higher level ones.
 * </p>
 */
public interface ImapResponseWriter {

    /**
     * Write a byte[] to the client
     * 
     * @param buffer
     *            <code>byte array</code> to be written, not null
     * @throws IOException
     */
    void write(byte[] buffer) throws IOException;

}
