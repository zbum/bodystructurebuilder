package kr.co.manty.mail.imap;

import kr.co.manty.mail.imap.model.BodyStructure;
import kr.co.manty.mail.imap.model.MultipartBodyStructure;
import kr.co.manty.mail.imap.model.SingleBodyStructure;
import kr.co.manty.mail.imap.model.TextBodyStructure;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.dom.field.ContentTransferEncodingField;
import org.apache.james.mime4j.dom.field.ContentTypeField;
import org.apache.james.mime4j.dom.field.ParsedField;
import org.apache.james.mime4j.field.DefaultFieldParser;
import org.apache.james.mime4j.stream.*;

import java.io.*;
import java.util.LinkedList;

public class BodyStructureBuilder {


    private BodyStructureBuilder() {
    }

    public static String build(InputStream mimeInputStream) {
        MultipartBodyStructure previous = null;
        SingleBodyStructure currentBodyStructure = null;

        MimeTokenStream stream = new MimeTokenStream();
        try (InputStream inputStream = mimeInputStream) {
            stream.parse(inputStream);
            for (EntityState state = stream.getState();
                 state != EntityState.T_END_OF_STREAM;
                 state = stream.next()) {

                switch (state) {
                    case T_BODY:
                        if (currentBodyStructure == null) break;

                        StreamAttribute streamAttribute = getStreamAttribute(stream.getInputStream());
                        currentBodyStructure.setSize(streamAttribute.size);
                        if (currentBodyStructure instanceof TextBodyStructure) {
                            ((TextBodyStructure) currentBodyStructure).setLine(streamAttribute.line);
                        }

                        break;
                    case T_END_MULTIPART:
                        System.out.println(previous.serialize());
                        break;

                    case T_START_MESSAGE:
                        System.out.println("message start");
                        break;
                    case T_FIELD:
                        Field rawField = stream.getField();
                        ParsedField field = DefaultFieldParser.parse(new String(rawField.getRaw().toByteArray(), "UTF-8"));
                        if (field instanceof ContentTypeField) {
                            ContentTypeField contentTypeField = (ContentTypeField) field;
                            if (contentTypeField.isMultipart()) {

                                previous = MultipartBodyStructure.builder()
                                        .parts(new LinkedList<>())
                                        .subtype(contentTypeField.getSubType())
                                        .parameters(contentTypeField.getParameters())
                                        .build();

                            } else {
                                if ("text".equalsIgnoreCase(contentTypeField.getMediaType())) {
                                    currentBodyStructure = new TextBodyStructure(contentTypeField.getMediaType(),
                                            contentTypeField.getSubType(),
                                            contentTypeField.getParameters(),
                                            null,
                                            null,
                                            null,
                                            null,
                                            null);
                                } else {
                                    currentBodyStructure = new SingleBodyStructure(contentTypeField.getMediaType(),
                                            contentTypeField.getSubType(),
                                            contentTypeField.getParameters(),
                                            null,
                                            null,
                                            null,
                                            null);
                                }
                                if (previous != null) {
                                    previous.getParts().add(currentBodyStructure);
                                }
                            }
                        }

                        if (field instanceof ContentTransferEncodingField) {
                            currentBodyStructure.setEncoding(((ContentTransferEncodingField) field).getEncoding());
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

    private static StreamAttribute getStreamAttribute(InputStream inputStream) {
        long size = 0;
        long line = 1;
        int ch;
        while (true) {
            try {
                if (!((ch = inputStream.read()) != -1)) break;
            } catch (IOException e) {
                return null;
            }
            if (ch == '\n') {
                line++;
            }
            size++;
        }

        return new StreamAttribute(size, line);
    }

    @Data
    @AllArgsConstructor
    static class StreamAttribute {
        private long size;
        private long line;
    }

}
