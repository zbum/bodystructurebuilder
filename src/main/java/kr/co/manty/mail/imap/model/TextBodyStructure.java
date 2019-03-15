package kr.co.manty.mail.imap.model;

import lombok.Data;

import java.util.Map;
import java.util.StringJoiner;

@Data
public class TextBodyStructure extends SingleBodyStructure {
    private Long line;

    public TextBodyStructure(String type, String subtype, Map<String, String> parameters, String id, String description, String encoding, Long size, Long line) {
        super(type, subtype, parameters, id, description, encoding, size);
        this.line = line;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public String serialize() {
        StringJoiner joiner = new StringJoiner(" ");
        StringJoiner string = joiner.add(parenthesis(nil(this.getType())))
                .add(parenthesis(nil(this.getSubtype())))
                .add(nil(paramListSerialize(this.getParameters())))
                .add(parenthesis(nil(this.getId())))
                .add(parenthesis(nil(this.getDescription())))
                .add(parenthesis(nil(this.getEncoding())))
                .add(nil(this.getSize()))
                .add(nil(this.line));
        return "("+string.toString()+")";
    }
}
