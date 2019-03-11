package kr.co.manty.mail.imap;

import kr.co.manty.mail.imap.model.Tree;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.stream.BodyDescriptor;
import org.apache.james.mime4j.stream.EntityState;
import org.apache.james.mime4j.stream.MimeTokenStream;

import java.io.IOException;
import java.io.InputStream;

public class BodyStructureBuilder {


    private BodyStructureBuilder() {
    }

    public static String build(InputStream mimeInputStream) {
        Tree<BodyDescriptor> partTree = null;

        MimeTokenStream stream = new MimeTokenStream();
        try (InputStream inputStream = mimeInputStream) {
            stream.parse(inputStream);
            for (EntityState state = stream.getState();
                 state != EntityState.T_END_OF_STREAM;
                 state = stream.next()) {

                boolean multipartStarted = false;
                boolean messageStarted=false;
                
                switch (state) {
                    case T_BODY:
                    case T_START_MULTIPART:
                        if (partTree == null) {
                            partTree = new Tree<>(stream.getBodyDescriptor());
                        }
                        
                        if (partTree != null) {
                            
                        }
                        break;
                    case T_START_MESSAGE:
                        System.out.println("message start");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (MimeException e) {
            throw new RuntimeException(e);
        }


        return "";

    }
}
