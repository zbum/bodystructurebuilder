package kr.co.manty.mail.imap.bodystructure;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class BodyStructureTest {
    @Test
    @DisplayName("test multipart alternative mime")
    void testMultipartAlternativeMime() throws IOException {
        InputStream mimeInputStream = getClass().getResourceAsStream("/eml/test1.eml");
        InputStream expectInputStream = getClass().getResourceAsStream("/result/test1.bodystructure");

        byte[] actual = BodyStructureBuilder.build(mimeInputStream, true);

        assertArrayEquals(actual, IOUtils.toByteArray(expectInputStream));
    }

    @Test
    @DisplayName("test multipart mixed mime")
    void testMultipartMixedMime() throws IOException {
        InputStream mimeInputStream = getClass().getResourceAsStream("/eml/complex.eml");
        InputStream expectInputStream = getClass().getResourceAsStream("/result/complex.bodystructure");

        byte[] actual = BodyStructureBuilder.build(mimeInputStream, true);

        assertArrayEquals(actual, IOUtils.toByteArray(expectInputStream));
    }

    @Test
    @DisplayName("test multipart report mime")
    void testMultipartReport() throws IOException {
        //given
        InputStream mimeInputStream = getClass().getResourceAsStream("/eml/multipart_report.eml");
        InputStream expectInputStream = getClass().getResourceAsStream("/result/multipart_report.bodystructure");

        //when
        byte[] actual = BodyStructureBuilder.build(mimeInputStream, true);

        System.out.println(new String(actual));

        //then
        assertArrayEquals(actual, IOUtils.toByteArray(expectInputStream));
    }
}