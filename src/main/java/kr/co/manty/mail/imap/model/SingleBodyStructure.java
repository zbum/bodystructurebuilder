package kr.co.manty.mail.imap.model;

import lombok.Data;

import java.util.Map;
import java.util.StringJoiner;

@Data
public class SingleBodyStructure implements BodyStructure {
    private String type;
    private String subtype;
    private Map<String, String> parameters;
    private String id;
    private String description;
    private String encoding;
    private Long size;

    public SingleBodyStructure(String type, String subtype, Map<String, String> parameters, String id, String description, String encoding, Long size) {
        this.type = type;
        this.subtype = subtype;
        this.parameters = parameters;
        this.id = id;
        this.description = description;
        this.encoding = encoding;
        this.size = size;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public String serialize() {
        StringJoiner joiner = new StringJoiner(" ");
        StringJoiner string = joiner.add(parenthesis(nil(this.getType())))
                .add(parenthesis(nil(this.getSubtype())))
                .add(nil(paramListSerialize(this.getParameters())))
                .add(parenthesis(nil(this.getId())))
                .add(parenthesis(nil(this.getDescription())))
                .add(parenthesis(nil(this.getEncoding())))
                .add(nil(this.getSize()));
        return "("+string.toString()+")";
    }

    @Override
    public String bodySerialize() {
        return serialize();
    }
}
