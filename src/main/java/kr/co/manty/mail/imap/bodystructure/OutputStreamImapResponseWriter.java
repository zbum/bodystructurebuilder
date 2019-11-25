package kr.co.manty.mail.imap.bodystructure;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Class providing methods to send response messages from the server to the
 * client.
 */
class OutputStreamImapResponseWriter implements ImapResponseWriter {

    private final OutputStream output;

    OutputStreamImapResponseWriter(OutputStream output) {
        this.output = output;
    }

    public void flush() throws IOException {
        output.flush();
    }

    @Override
    public void write(byte[] buffer) throws IOException {
        output.write(buffer);
    }

}
