package kr.co.manty.mail.imap.bodystructure;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BodyStructureTest {
    @Test
    public void build() throws IOException {
        InputStream mimeInputStream = getClass().getResourceAsStream("/eml/test1.eml");
        InputStream expectInputStream = getClass().getResourceAsStream("/result/test1.bodystructure");

        byte[] actual = BodyStructureBuilder.build(mimeInputStream);

        assertThat(actual, is(IOUtils.toByteArray(expectInputStream)));

    }

    @Test
    public void buildComplex() throws IOException {
        InputStream mimeInputStream = getClass().getResourceAsStream("/eml/complex.eml");
        InputStream expectInputStream = getClass().getResourceAsStream("/result/complex.bodystructure");

        byte[] actual = BodyStructureBuilder.build(mimeInputStream);

        assertThat(actual, is(IOUtils.toByteArray(expectInputStream)));
    }

    @Test
    public void buildMultipartReport() throws IOException {
        InputStream mimeInputStream = getClass().getResourceAsStream("/eml/multipart_report.eml");
        InputStream expectInputStream = getClass().getResourceAsStream("/result/multipart_report.bodystructure");

        byte[] actual = BodyStructureBuilder.build(mimeInputStream);

        assertThat(actual, is(IOUtils.toByteArray(expectInputStream)));
    }
}