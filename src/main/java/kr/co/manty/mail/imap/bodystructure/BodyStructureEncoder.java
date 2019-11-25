package kr.co.manty.mail.imap.bodystructure;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;

@Slf4j
public class BodyStructureEncoder {

    static final String ENVELOPE = "ENVELOPE";

    void encodeStructure(ImapResponseComposer composer, FetchResponse.Structure structure, boolean includeExtensions, boolean isInnerPart) throws IOException {

        final String mediaType;
        final String subType;
        final String rawMediaType = structure.getMediaType();
        if (rawMediaType == null) {
            mediaType = ImapConstants.MIME_TYPE_TEXT;
            subType = ImapConstants.MIME_SUBTYPE_PLAIN;
        } else {
            mediaType = rawMediaType;
            subType = structure.getSubType();
        }
        encodeStructure(composer, structure, includeExtensions, mediaType, subType, isInnerPart);
    }

    private void encodeStructure(ImapResponseComposer composer, FetchResponse.Structure structure, boolean includeExtensions, String mediaType, String subType, boolean isInnerPart) throws IOException {
        //
        // Workaround for broken clients
        // See IMAP-91
        //
        if (isInnerPart) {
            composer.skipNextSpace();
        }
        if (ImapConstants.MIME_TYPE_MULTIPART.equalsIgnoreCase(mediaType)) {

            encodeMultipart(composer, structure, subType, includeExtensions);

        } else {
            if (ImapConstants.MIME_TYPE_MESSAGE.equalsIgnoreCase(mediaType) && ImapConstants.MIME_SUBTYPE_RFC822.equalsIgnoreCase(subType)) {

                encodeRfc822Message(composer, structure, mediaType, subType, includeExtensions);
            } else {
                encodeBasic(composer, structure, includeExtensions, mediaType, subType);
            }
        }
    }

    private void encodeBasic(ImapResponseComposer composer, FetchResponse.Structure structure, boolean includeExtensions, String mediaType, String subType) throws IOException {
        if (ImapConstants.MIME_TYPE_TEXT.equalsIgnoreCase(mediaType)) {

            final long lines = structure.getLines();

            encodeBodyFields(composer, structure, mediaType, subType);
            composer.message(lines);
        } else {
            encodeBodyFields(composer, structure, mediaType, subType);
        }
        if (includeExtensions) {
            encodeOnePartBodyExtensions(composer, structure);
        }
        composer.closeParen();
    }

    private void encodeOnePartBodyExtensions(ImapResponseComposer composer, FetchResponse.Structure structure) throws IOException {
        final String md5 = structure.getMD5();
        final List<String> languages = structure.getLanguages();
        final String location = structure.getLocation();
        nillableQuote(composer, md5);
        bodyFldDsp(structure, composer);
        nillableQuotes(composer, languages);
        nillableQuote(composer, location);
    }

    private ImapResponseComposer bodyFldDsp(FetchResponse.Structure structure, ImapResponseComposer composer) throws IOException {
        final String disposition = structure.getDisposition();
        if (disposition == null) {
            composer.nil();
        } else {
            composer.openParen();
            composer.quote(disposition);
            final Map<String, String> params = structure.getDispositionParams();
            bodyFldParam(params, composer);
            composer.closeParen();
        }
        return composer;
    }

    private void bodyFldParam(Map<String, String> params, ImapResponseComposer composer) throws IOException {
        if (params == null || params.isEmpty()) {
            composer.nil();
        } else {
            composer.openParen();
            final Set<String> keySet = params.keySet();
            final Collection<String> names = new TreeSet<>(keySet);
            for (String name : names) {
                final String value = params.get(name);
                if (value == null) {
                    log.warn("Disposition parameter name has no value.");
                    log.debug("Disposition parameter {} has no matching value", name);
                } else {
                    composer.quote(name);
                    composer.quote(value);
                }
            }
            composer.closeParen();
        }
    }

    private void encodeBodyFields(ImapResponseComposer composer, FetchResponse.Structure structure, String mediaType, String subType) throws IOException {
        final List<String> bodyParams = structure.getParameters();
        final String id = structure.getId();
        final String description = structure.getDescription();
        final String encoding = structure.getEncoding();
        final long octets = structure.getOctets();
        composer.openParen().quoteUpperCaseAscii(mediaType).quoteUpperCaseAscii(subType);
        nillableQuotes(composer, bodyParams);
        nillableQuote(composer, id);
        nillableQuote(composer, description);
        composer.quoteUpperCaseAscii(encoding).message(octets);
    }

    private void encodeMultipart(ImapResponseComposer composer, FetchResponse.Structure structure, String subType, boolean includeExtensions) throws IOException {
        composer.openParen();

        for (Iterator<FetchResponse.Structure> it = structure.parts(); it.hasNext();) {
            final FetchResponse.Structure part = it.next();
            encodeStructure(composer, part, includeExtensions, true);
        }

        composer.quoteUpperCaseAscii(subType);
        if (includeExtensions) {
            final List<String> languages = structure.getLanguages();
            nillableQuotes(composer, structure.getParameters());
            bodyFldDsp(structure, composer);
            nillableQuotes(composer, languages);
            nillableQuote(composer, structure.getLocation());
        }
        composer.closeParen();
    }

    private void encodeRfc822Message(ImapResponseComposer composer, FetchResponse.Structure structure, String mediaType, String subType, boolean includeExtensions) throws IOException {
        final long lines = structure.getLines();
        final FetchResponse.Envelope envelope = structure.getEnvelope();
        final FetchResponse.Structure embeddedStructure = structure.getBody();

        encodeBodyFields(composer, structure, mediaType, subType);
        encodeEnvelope(composer, envelope, false);
        encodeStructure(composer, embeddedStructure, includeExtensions, false);
        composer.message(lines);

        if (includeExtensions) {
            encodeOnePartBodyExtensions(composer, structure);
        }
        composer.closeParen();
    }



    private void encodeEnvelope(ImapResponseComposer composer, FetchResponse.Envelope envelope, boolean prefixWithName) throws IOException {
        if (envelope != null) {
            final String date = envelope.getDate();
            final String subject = envelope.getSubject();
            final FetchResponse.Envelope.Address[] from = envelope.getFrom();
            final FetchResponse.Envelope.Address[] sender = envelope.getSender();
            final FetchResponse.Envelope.Address[] replyTo = envelope.getReplyTo();
            final FetchResponse.Envelope.Address[] to = envelope.getTo();
            final FetchResponse.Envelope.Address[] cc = envelope.getCc();
            final FetchResponse.Envelope.Address[] bcc = envelope.getBcc();
            final String inReplyTo = envelope.getInReplyTo();
            final String messageId = envelope.getMessageId();

            if (prefixWithName) {
                composer.message(ENVELOPE);
            }
            composer.openParen();
            nillableQuote(composer, date);
            nillableQuote(composer, subject);
            encodeAddresses(composer, from);
            encodeAddresses(composer, sender);
            encodeAddresses(composer, replyTo);
            encodeAddresses(composer, to);
            encodeAddresses(composer, cc);
            encodeAddresses(composer, bcc);

            nillableQuote(composer, inReplyTo);
            nillableQuote(composer, messageId);
            composer.closeParen();
        }
    }

    private void encodeAddresses(ImapResponseComposer composer, FetchResponse.Envelope.Address[] addresses) throws IOException {
        if (addresses == null || addresses.length == 0) {
            composer.nil();
        } else {
            composer.openParen();
            for (FetchResponse.Envelope.Address address : addresses) {
                encodeAddress(composer, address);
            }
            composer.closeParen();
        }
    }

    private void encodeAddress(ImapResponseComposer composer, FetchResponse.Envelope.Address address) throws IOException {
        final String name = address.getPersonalName();
        final String domainList = address.getAtDomainList();
        final String mailbox = address.getMailboxName();
        final String host = address.getHostName();
        composer.skipNextSpace().openParen();
        nillableQuote(composer, name);
        nillableQuote(composer, domainList);
        nillableQuote(composer, mailbox);
        nillableQuote(composer,host).closeParen();
    }



    private ImapResponseComposer nillableQuote(ImapResponseComposer composer, String message) throws IOException {
        if (message == null) {
            composer.nil();
        } else {
            composer.quote(message);
        }
        return composer;
    }


    private ImapResponseComposer nillableQuotes(ImapResponseComposer composer, List<String> quotes) throws IOException {
        if (quotes == null || quotes.isEmpty()) {
            composer.nil();
        } else {
            composer.openParen();
            for (String string : quotes) {
                nillableQuote(composer,string);
            }
            composer.closeParen();
        }
        return composer;
    }
}
