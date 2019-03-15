package kr.co.manty.mail.imap.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface BodyStructure {
    String serialize();
    String bodySerialize();
    
    default String listSerialize(List<BodyStructure> list) {
        String str;
        if (list == null) {
            str = "NIL";
        } else {
            str = list.stream()
                      .map(it -> it.serialize())
                      .map(it -> "(" + it + ")")
                      .collect(Collectors.joining());
        }

        return str;
    }

    default String paramListSerialize(Map<String, String> parameters) {
        String parameterStr;
        if (parameters == null || parameters.size() == 0) {
            parameterStr = null;
        } else {
            parameterStr = "(" +
                parameters.entrySet().stream()
                          .map(it -> "\""+it.getKey() + "\" \"" + it.getValue()+"\"")
                          .collect(Collectors.joining(" "))
                + ")";
        }
        return parameterStr;
    }

    default String nil(String string) {
        return string == null ? "NIL" : string;
    }

    default String nil(Long longValue) {
        return longValue == null ? "NIL" : longValue.toString();
    }

}
