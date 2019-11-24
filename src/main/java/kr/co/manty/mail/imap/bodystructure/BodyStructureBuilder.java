package kr.co.manty.mail.imap.bodystructure;

import org.apache.james.imap.decode.main.OutputStreamImapResponseWriter;
import org.apache.james.imap.encode.ImapResponseComposer;
import org.apache.james.imap.encode.base.ImapResponseComposerImpl;
import org.apache.james.imap.processor.fetch.EnvelopeBuilder;
import org.apache.james.mailbox.model.MimeDescriptor;
import org.apache.james.mailbox.store.MimeDescriptorImpl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class BodyStructureBuilder {

    public static byte[] build(InputStream mimeInputStream) {
        MimeDescriptor mimeDescriptor;
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()){
            mimeDescriptor = MimeDescriptorImpl.build(mimeInputStream);

            MimeDescriptorStructure mimeDescriptorStructure = new MimeDescriptorStructure(true, mimeDescriptor, new EnvelopeBuilder());

            OutputStreamImapResponseWriter writer = new OutputStreamImapResponseWriter(output);
            ImapResponseComposer bodyStructureComposer = new BodyStructureComposer(writer);
            new BodyStructureEncoder().encodeStructure(bodyStructureComposer, mimeDescriptorStructure, true, true);
            bodyStructureComposer.end();

            return output.toByteArray();

        } catch (Exception e) {
            throw new BodyStructureBuildException(e);
        }

    }
}
