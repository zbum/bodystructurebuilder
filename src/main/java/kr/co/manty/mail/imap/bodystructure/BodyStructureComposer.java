package kr.co.manty.mail.imap.bodystructure;

import org.apache.james.imap.api.ImapCommand;
import org.apache.james.imap.api.ImapConstants;
import org.apache.james.imap.api.display.CharsetUtil;
import org.apache.james.imap.api.message.IdRange;
import org.apache.james.imap.api.message.UidRange;
import org.apache.james.imap.encode.ImapResponseComposer;
import org.apache.james.imap.encode.ImapResponseWriter;
import org.apache.james.imap.message.response.Literal;
import org.apache.james.protocols.imap.utils.FastByteArrayOutputStream;

import javax.mail.Flags;
import java.io.IOException;
import java.nio.charset.Charset;

public class BodyStructureComposer implements org.apache.james.imap.api.ImapConstants, ImapResponseComposer {

    public static final String FLAGS = "FLAGS";

    public static final String FAILED = "failed.";
    private static final int LOWER_CASE_OFFSET = 'a' - 'A';
    public static final int DEFAULT_BUFFER_SIZE = 2048;


    private final ImapResponseWriter writer;

    private final FastByteArrayOutputStream buffer;

    private final Charset usAscii;

    private boolean skipNextSpace;

    public BodyStructureComposer(ImapResponseWriter writer, int bufferSize) {
        skipNextSpace = false;
        usAscii = Charset.forName("US-ASCII");
        this.writer = writer;
        this.buffer = new FastByteArrayOutputStream(bufferSize);
    }

    public BodyStructureComposer(ImapResponseWriter writer) {
        this(writer, DEFAULT_BUFFER_SIZE);
    }

    @Override
    public ImapResponseComposer untaggedNoResponse(String displayMessage, String responseCode) throws IOException {
        untagged();
        message(NO);
        responseCode(responseCode);
        message(displayMessage);
        end();
        return this;
    }

    @Override
    public ImapResponseComposer continuationResponse(String message) throws IOException {
        writeASCII(CONTINUATION + SP + message);
        end();
        return this;
    }



    @Override
    public ImapResponseComposer commandResponse(ImapCommand command, String message) throws IOException {
        untagged();
        commandName(command.getName());
        message(message);
        end();
        return this;
    }

    @Override
    public ImapResponseComposer taggedResponse(String message, String tag) throws IOException {
        tag(tag);
        message(message);
        end();
        return this;
    }

    @Override
    public ImapResponseComposer untaggedResponse(String message) throws IOException {
        untagged();
        message(message);
        end();
        return this;
    }


    @Override
    public ImapResponseComposer untagged() throws IOException {
        writeASCII(UNTAGGED);
        return this;
    }

    @Override
    public ImapResponseComposer message(String message) throws IOException {
        if (message != null) {
            // TODO: consider message normalisation
            // TODO: CR/NFs in message must be replaced
            // TODO: probably best done in the writer
            space();
            writeASCII(message);

        }
        return this;
    }

    private void responseCode(String responseCode) throws IOException {
        if (responseCode != null && !"".equals(responseCode)) {
            writeASCII(" [");
            writeASCII(responseCode);
            buffer.write(BYTE_CLOSE_SQUARE_BRACKET);
        }
    }

    @Override
    public ImapResponseComposer end() throws IOException {
        writer.write(buffer.toByteArray());
        buffer.reset();
        return this;
    }

    @Override
    public ImapResponseComposer tag(String tag) throws IOException {
        writeASCII(tag);
        return this;
    }

    @Override
    public ImapResponseComposer closeParen() throws IOException {
        closeBracket(BYTE_CLOSING_PARENTHESIS);
        return this;
    }

    @Override
    public ImapResponseComposer openParen() throws IOException {
        openBracket(BYTE_OPENING_PARENTHESIS);
        return this;
    }

    @Override
    public ImapResponseComposer flags(Flags flags) throws IOException {
        message(FLAGS);
        openParen();
        if (flags.contains(Flags.Flag.ANSWERED)) {
            message("\\Answered");
        }
        if (flags.contains(Flags.Flag.DELETED)) {
            message("\\Deleted");
        }
        if (flags.contains(Flags.Flag.DRAFT)) {
            message("\\Draft");
        }
        if (flags.contains(Flags.Flag.FLAGGED)) {
            message("\\Flagged");
        }
        if (flags.contains(Flags.Flag.RECENT)) {
            message("\\Recent");
        }
        if (flags.contains(Flags.Flag.SEEN)) {
            message("\\Seen");
        }

        String[] userFlags = flags.getUserFlags();
        for (String userFlag : userFlags) {
            message(userFlag);
        }
        closeParen();
        return this;
    }

    @Override
    public ImapResponseComposer nil() throws IOException {
        message(NIL);
        return this;
    }

    @Override
    public ImapResponseComposer upperCaseAscii(String message) throws IOException {
        if (message == null) {
            nil();
        } else {
            upperCaseAscii(message, false);
        }
        return this;
    }

    @Override
    public ImapResponseComposer quoteUpperCaseAscii(String message) throws IOException {
        if (message == null) {
            nil();
        } else {
            upperCaseAscii(message, true);
        }
        return this;
    }


    private void writeASCII(String string) throws IOException {
        buffer.write(string.getBytes(usAscii));
    }

    @Override
    public ImapResponseComposer message(long number) throws IOException {
        space();
        writeASCII(Long.toString(number));
        return this;
    }

    @Override
    public ImapResponseComposer mailbox(String mailboxName) throws IOException {
        quote(CharsetUtil.encodeModifiedUTF7(mailboxName));
        return this;
    }

    @Override
    public ImapResponseComposer commandName(String commandName) throws IOException {
        space();
        writeASCII(commandName);
        return this;
    }

    @Override
    public ImapResponseComposer quote(String message) throws IOException {
        space();
        final int length = message.length();

        buffer.write(BYTE_DQUOTE);
        for (int i = 0; i < length; i++) {
            char character = message.charAt(i);
            if (character == ImapConstants.BACK_SLASH || character == DQUOTE) {
                buffer.write(BYTE_BACK_SLASH);
            }
            // 7-bit ASCII only
            if (character > 128) {
                buffer.write(BYTE_QUESTION);
            } else {
                buffer.write((byte) character);
            }
        }
        buffer.write(BYTE_DQUOTE);
        return this;
    }


    private void closeBracket(byte bracket) throws IOException {
        buffer.write(bracket);
        clearSkipNextSpace();
    }

    private void openBracket(byte bracket) throws IOException {
        space();
        buffer.write(bracket);
        skipNextSpace();
    }

    private void clearSkipNextSpace() {
        skipNextSpace = false;
    }

    @Override
    public ImapResponseComposer skipNextSpace() {
        skipNextSpace = true;
        return this;
    }

    private void space() throws IOException {
        if (skipNextSpace) {
            skipNextSpace = false;
        } else {
            buffer.write(SP.getBytes());
        }
    }

    @Override
    public ImapResponseComposer literal(Literal literal) throws IOException {
        space();
        buffer.write(BYTE_OPEN_BRACE);
        final long size = literal.size();
        writeASCII(Long.toString(size));
        buffer.write(BYTE_CLOSE_BRACE);
        end();
        if (size > 0) {
            writer.write(literal);
        }
        return this;
    }

    @Override
    public ImapResponseComposer closeSquareBracket() throws IOException {
        closeBracket(BYTE_CLOSE_SQUARE_BRACKET);
        return this;
    }

    @Override
    public ImapResponseComposer openSquareBracket() throws IOException {
        openBracket(BYTE_OPEN_SQUARE_BRACKET);
        return this;
    }

    private void upperCaseAscii(String message, boolean quote) throws IOException {
        space();
        final int length = message.length();
        if (quote) {
            buffer.write(BYTE_DQUOTE);
        }
        for (int i = 0; i < length; i++) {
            final char next = message.charAt(i);
            if (next >= 'a' && next <= 'z') {
                buffer.write((byte) (next - LOWER_CASE_OFFSET));
            } else {
                buffer.write((byte) (next));
            }
        }
        if (quote) {
            buffer.write(BYTE_DQUOTE);
        }
    }

    @Override
    public ImapResponseComposer sequenceSet(UidRange[] ranges) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ranges.length; i++) {
            UidRange range = ranges[i];
            sb.append(range.getFormattedString());
            if (i + 1 < ranges.length) {
                sb.append(",");
            }
        }
        return message(sb.toString());
    }

    @Override
    public ImapResponseComposer sequenceSet(IdRange[] ranges) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ranges.length; i++) {
            IdRange range = ranges[i];
            sb.append(range.getFormattedString());
            if (i + 1 < ranges.length) {
                sb.append(",");
            }
        }
        return message(sb.toString());
    }

}