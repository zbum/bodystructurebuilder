package kr.co.manty.mail.imap;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * @author jibumjung
 * @since
 */
public class BodyStructureBuilderTest {

    @Test
    public void build() {
        InputStream mimeInputStream = getClass().getResourceAsStream("/eml/test1.eml");
        BodyStructureBuilder.build(mimeInputStream);
    }

    @Test
    public void buildComplex() {
        InputStream mimeInputStream = getClass().getResourceAsStream("/eml/complex.eml");
        BodyStructureBuilder.build(mimeInputStream);
    }

    @Test
    public void buildMultipartReport() {
        InputStream mimeInputStream = getClass().getResourceAsStream("/eml/multipart_report.eml");
        BodyStructureBuilder.build(mimeInputStream);
    }
}