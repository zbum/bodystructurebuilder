package kr.co.manty.mail.imap.bodystructure;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BodyStructureTest {
    @ParameterizedTest
    @MethodSource("provideTestData")
    @DisplayName("test body structure builder")
    void testBodyStructure(String emlFile, String expectedFile, String displayName) throws IOException {
        InputStream mimeInputStream = getClass().getResourceAsStream("/eml/" + emlFile);
        InputStream expectInputStream = getClass().getResourceAsStream("/result/" + expectedFile);

        byte[] actual = BodyStructureBuilder.build(mimeInputStream, true);

        assertNotNull(expectInputStream, "Expected file should not be null: " + expectedFile);
        assertArrayEquals(IOUtils.toByteArray(expectInputStream), actual, 
                "Body structure mismatch for: " + displayName);
    }

    static Stream<Arguments> provideTestData() {
        return Stream.of(
                Arguments.of("test1.eml", "test1.bodystructure", "multipart alternative mime"),
                Arguments.of("complex.eml", "complex.bodystructure", "multipart mixed mime"),
                Arguments.of("multipart_report.eml", "multipart_report.bodystructure", "multipart report mime"),
                Arguments.of("simple_text.eml", "simple_text.bodystructure", "simple text/plain mime"),
                Arguments.of("html_only.eml", "html_only.bodystructure", "text/html mime"),
                Arguments.of("multipart_related.eml", "multipart_related.bodystructure", "multipart related with embedded image"),
                Arguments.of("with_attachment.eml", "with_attachment.bodystructure", "multipart mixed with attachment"),
                Arguments.of("embedded_message.eml", "embedded_message.bodystructure", "message/rfc822 embedded message")
        );
    }
}