package kr.co.manty.mail.imap.bodystructure;


import java.io.IOException;
import java.io.InputStream;

/**
 * IMAP needs to know the size of the content before it starts to write it out.
 * This interface allows direct writing whilst exposing total size.
 */
public interface Content {
    InputStream getInputStream() throws IOException;
    
    long size();
}