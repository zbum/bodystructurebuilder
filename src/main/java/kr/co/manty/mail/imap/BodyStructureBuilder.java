package kr.co.manty.mail.imap;

import kr.co.manty.mail.imap.model.BodyStructure;
import kr.co.manty.mail.imap.model.MultipartBodyStructure;
import kr.co.manty.mail.imap.model.SingleBodyStructure;
import kr.co.manty.mail.imap.model.TextBodyStructure;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.dom.field.ContentTypeField;
import org.apache.james.mime4j.dom.field.ParsedField;
import org.apache.james.mime4j.field.DefaultFieldParser;
import org.apache.james.mime4j.stream.*;

import java.io.IOException;
import java.io.InputStream;

public class BodyStructureBuilder {


    private BodyStructureBuilder() {
    }

    public static String build(InputStream mimeInputStream) {
        BodyStructure currentBodyStructure = null;

        MimeTokenStream stream = new MimeTokenStream();
        try (InputStream inputStream = mimeInputStream) {
            stream.parse(inputStream);
            for (EntityState state = stream.getState();
                 state != EntityState.T_END_OF_STREAM;
                 state = stream.next()) {

                switch (state) {
                    case T_BODY:
                        if (currentBodyStructure instanceof SingleBodyStructure) {
                            ((SingleBodyStructure)currentBodyStructure).setSize(stream.getBodyDescriptor().getContentLength());
                        }
                        System.out.println(currentBodyStructure.serialize());
                        break;
                    case T_END_MULTIPART:
                        break;
                        
                    case T_START_MESSAGE:
                        System.out.println("message start");
                        break;
                    case T_FIELD:
                        Field rawField = stream.getField();
                        ParsedField field = DefaultFieldParser.parse(new String(rawField.getRaw().toByteArray(), "UTF-8"));
                        if (field instanceof ContentTypeField) {
                            ContentTypeField contentTypeField = (ContentTypeField) field;
                            if ( contentTypeField.isMultipart()) {

                                currentBodyStructure = MultipartBodyStructure.builder()
                                                            .subtype(contentTypeField.getSubType())
                                                            .parameters(contentTypeField.getParameters())
                                                            .build();
                                
                            }else{
                                if ("text".equalsIgnoreCase(contentTypeField.getMediaType())) {

                                    currentBodyStructure= new TextBodyStructure(contentTypeField.getMediaType(),
                                                                contentTypeField.getSubType(), 
                                                                contentTypeField.getParameters(),
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                null);
                                }else{

                                    currentBodyStructure= new SingleBodyStructure(contentTypeField.getMediaType(),
                                                              contentTypeField.getSubType(),
                                                              contentTypeField.getParameters(),
                                                              null,
                                                              null,
                                                              null,
                                                              null);
                                }
                            }
                                
                        }
                        break;

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
