package kr.co.manty.mail.imap.bodystructure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kr.co.manty.mail.imap.bodystructure.mime.MimeDescriptor;
import kr.co.manty.mail.imap.envelope.Envelope;
import kr.co.manty.mail.imap.envelope.EnvelopeBuilder;
import lombok.ToString;

@ToString(callSuper = true)
final class MimeDescriptorStructure implements Structure {

    private final MimeDescriptor descriptor;

    private final List<String> parameters;

    private final List<Structure> parts;

    private final String disposition;

    private final Map<String, String> dispositionParams;

    private final String location;

    private final String md5;

    private final List<String> languages;

    private final Structure embeddedMessageStructure;

    private final Envelope envelope;

    MimeDescriptorStructure(boolean allowExtensions, MimeDescriptor descriptor, EnvelopeBuilder builder)  {
        super();
        this.descriptor = descriptor;
        parameters = createParameters(descriptor);
        parts = createParts(allowExtensions, descriptor, builder);

        languages = descriptor.getLanguages();
        this.dispositionParams = descriptor.getDispositionParams();
        this.disposition = descriptor.getDisposition();

        this.md5 = descriptor.getContentMD5();
        this.location = descriptor.getContentLocation();

        final MimeDescriptor embeddedMessage = descriptor.embeddedMessage();
        if (embeddedMessage == null) {
            embeddedMessageStructure = null;
            envelope = null;
        } else {
            embeddedMessageStructure = new MimeDescriptorStructure(allowExtensions, embeddedMessage, builder);
            envelope = builder.buildEnvelope(embeddedMessage);
        }
    }

    private static List<Structure> createParts(boolean allowExtensions, MimeDescriptor descriptor, EnvelopeBuilder builder)  {
        final List<Structure> results = new ArrayList<>();
        for (Iterator<MimeDescriptor> it = descriptor.parts(); it.hasNext();) {
            final MimeDescriptor partDescriptor = it.next();
            results.add(new MimeDescriptorStructure(allowExtensions, partDescriptor, builder));
        }
        return results;
    }

    private static List<String> createParameters(MimeDescriptor descriptor) {
        final List<String> results = new ArrayList<>();
        // TODO: consider revising this
        for (Map.Entry<String, String> entry : descriptor.contentTypeParameters().entrySet()) {
            results.add(entry.getKey());
            results.add(entry.getValue());
        }
        return results;
    }

    @Override
    public String getDescription() {
        return descriptor.getContentDescription();
    }

    @Override
    public String getEncoding() {
        return descriptor.getTransferContentEncoding();
    }

    @Override
    public String getId() {
        return descriptor.getContentID();
    }

    @Override
    public long getLines() {
        return descriptor.getLines();
    }

    @Override
    public String getMediaType() {
        return descriptor.getMimeType();
    }

    @Override
    public long getOctets() {
        return descriptor.getBodyOctets();
    }

    @Override
    public List<String> getParameters() {
        return parameters;
    }

    @Override
    public String getSubType() {
        return descriptor.getMimeSubType();
    }

    @Override
    public Iterator<Structure> parts() {
        return parts.iterator();
    }

    @Override
    public String getDisposition() {
        return disposition;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public String getMD5() {
        return md5;
    }

    @Override
    public List<String> getLanguages() {
        return languages;
    }

    @Override
    public Structure getBody() {
        return embeddedMessageStructure;
    }

    @Override
    public Map<String, String> getDispositionParams() {
        return dispositionParams;
    }

    @Override
    public Envelope getEnvelope() {
        return envelope;
    }

}
