package kr.co.manty.mail.imap.bodystructure;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class GenerateBodyStructureResults {
    public static void main(String[] args) throws IOException {
        String[] emlFiles = {
            "simple_text.eml",
            "html_only.eml",
            "multipart_related.eml",
            "with_attachment.eml",
            "embedded_message.eml"
        };

        String baseDir = "src/test/resources";
        
        for (String emlFile : emlFiles) {
            String emlPath = baseDir + "/eml/" + emlFile;
            String resultPath = baseDir + "/result/" + emlFile.replace(".eml", ".bodystructure");
            
            File emlFileObj = new File(emlPath);
            if (!emlFileObj.exists()) {
                System.out.println("Skipping " + emlFile + " (file not found)");
                continue;
            }
            
            try (InputStream input = new FileInputStream(emlFileObj);
                 FileOutputStream output = new FileOutputStream(resultPath)) {
                byte[] result = BodyStructureBuilder.build(input, true);
                output.write(result);
                System.out.println("Generated: " + resultPath);
            }
        }
    }
}

