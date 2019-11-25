package kr.co.manty.mail.imap.bodystructure.mime;

import kr.co.manty.mail.imap.Header;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

final class ResultHeader implements Header {
    private static final Charset US_ASCII = StandardCharsets.US_ASCII;

    private final String name;
    private final String value;
    private final long size;

    ResultHeader(String name, String value) {
        this.name = name;
        this.value = value;
        size = name.length() + value.length() + 2;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public long size() {
        return size;
    }

    public String toString() {
        return "[HEADER " + name + ": " + value + "]";
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream((name + ": " + value).getBytes(US_ASCII));
    }
}