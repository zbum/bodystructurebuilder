package kr.co.manty.mail.imap.bodystructure;

import java.util.Iterator;

public interface Headers extends Content {
    Iterator<MessageResult.Header> headers() throws BodyStructureBuildException;
    
    
}
