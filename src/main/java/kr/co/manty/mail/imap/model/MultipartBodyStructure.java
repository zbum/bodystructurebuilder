package kr.co.manty.mail.imap.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Data
@Builder
public class MultipartBodyStructure implements BodyStructure {
    private List<BodyStructure> parts;
    private String subtype;
    private Map<String, String> parameters;
    private String disposition;
    private String language;
    private String location;

    @Override
    public String serialize() {
        StringJoiner joiner = new StringJoiner(" ");
        return joiner.add(nil(listSerialize(this.parts)))
                     .add(nil(this.subtype))
                     .add(nil(paramListSerialize(this.parameters)))
                     .add(nil(this.disposition))
                     .add(nil(this.language))
                     .add(nil(this.location))
                     .toString();
    }

    @Override
    public String bodySerialize() {
        StringJoiner joiner = new StringJoiner(" ");
        return joiner.add(nil(listSerialize(this.parts)))
                     .add(nil(this.subtype))
                     .add(nil(paramListSerialize(this.parameters)))
                     .toString();
    }
}
