package kr.co.manty.mail.imap.bodystructure;

import kr.co.manty.mail.imap.ImapConstants;
import kr.co.manty.mail.imap.bodystructure.utils.FastByteArrayOutputStream;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

class BodyStructureComposer implements ImapConstants, ImapResponseComposer {

    private static final int LOWER_CASE_OFFSET = 'a' - 'A';
    public static final int DEFAULT_BUFFER_SIZE = 2048;


    private final ImapResponseWriter writer;

    private final FastByteArrayOutputStream buffer;

    private final Charset usAscii;

    private boolean skipNextSpace;

    public BodyStructureComposer(ImapResponseWriter writer, int bufferSize) {
        skipNextSpace = false;
        usAscii = StandardCharsets.US_ASCII;
        this.writer = writer;
        this.buffer = new FastByteArrayOutputStream(bufferSize);
    }

    public BodyStructureComposer(ImapResponseWriter writer) {
        this(writer, DEFAULT_BUFFER_SIZE);
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

    @Override
    public ImapResponseComposer end() throws IOException {
        writer.write(buffer.toByteArray());
        buffer.reset();
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
    public ImapResponseComposer nil() throws IOException {
        message(NIL);
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


    private void closeBracket(byte bracket) {
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

}
