package kr.co.manty.mail.imap.model;

import java.util.Map;
import java.util.StringJoiner;


public class TextBodyStructure extends SingleBodyStructure {
    private Long line;

    public TextBodyStructure(String type, String subtype, Map<String, String> parameters, String id, String description, String encoding, Long size, Long line) {
        super(type, subtype, parameters, id, description, encoding, size);
        this.line = line;
    }

    @Override
    public String serialize() {
        StringJoiner joiner = new StringJoiner(" ");
        return joiner.add(super.serialize())
                     .add(nil(this.line))
                     .toString();
    }
}
