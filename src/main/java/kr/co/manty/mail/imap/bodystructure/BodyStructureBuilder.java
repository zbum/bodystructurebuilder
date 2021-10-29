package kr.co.manty.mail.imap.bodystructure;

import kr.co.manty.mail.imap.bodystructure.mime.MimeDescriptor;
import kr.co.manty.mail.imap.bodystructure.mime.MimeDescriptorImpl;
import kr.co.manty.mail.imap.envelope.EnvelopeBuilder;
import lombok.experimental.UtilityClass;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@UtilityClass
public class BodyStructureBuilder {

    public static byte[] build(InputStream mimeInputStream, boolean includeExtension) {
        MimeDescriptor mimeDescriptor;
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()){
            mimeDescriptor = MimeDescriptorImpl.build(mimeInputStream);

            MimeDescriptorStructure mimeDescriptorStructure = new MimeDescriptorStructure(true, mimeDescriptor, new EnvelopeBuilder());

            OutputStreamImapResponseWriter writer = new OutputStreamImapResponseWriter(output);
            ImapResponseComposer bodyStructureComposer = new BodyStructureComposer(writer);
            new BodyStructureEncoder().encodeStructure(bodyStructureComposer, mimeDescriptorStructure, includeExtension, true);
            bodyStructureComposer.end();

            return output.toByteArray();

        } catch (Exception e) {
            throw new BodyStructureBuildException(e);
        }

    }
}
