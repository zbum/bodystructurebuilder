package kr.co.manty.mail.imap.bodystructure.utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * {@link InputStream} implementation which just consume the the wrapped {@link InputStream} and count
 * the lines which are contained within the wrapped stream
 * 
 *
 */
public final class CountingInputStream extends InputStream {

    private final InputStream in;

    private int lineCount;

    private int octetCount;

    public CountingInputStream(InputStream in) {
        super();
        this.in = in;
    }

    @Override
    public int read() throws IOException {
        int next = in.read();
        if (next > 0) {
            octetCount++;
            if (next == '\r') {
                lineCount++;
            }
        }
        return next;
    }

    /**
     * Return the line count 
     * 
     * @return lineCount
     */
    public int getLineCount() {
        return lineCount;
    }

    /**
     * Return the octet count
     * 
     * @return octetCount
     */
    public int getOctetCount() {
        return octetCount;
    }
    
    /**
     * Reads - and discards - the rest of the stream
     * @throws IOException
     */
    public void readAll() throws IOException {
        while (read() > 0) {
            ;
        }
    }
}
