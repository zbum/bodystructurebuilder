package kr.co.manty.mail.imap.bodystructure.envelope;

import kr.co.manty.mail.imap.bodystructure.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.james.mime4j.codec.EncoderUtil;
import org.apache.james.mime4j.dom.address.*;
import org.apache.james.mime4j.field.address.LenientAddressParser;

import javax.mail.internet.MimeUtility;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class EnvelopeBuilder {

    public FetchResponse.Envelope buildEnvelope(Headers headers)  {
        final String date = headerValue(headers, ImapConstants.RFC822_DATE);
        final String subject = headerValue(headers, ImapConstants.RFC822_SUBJECT);
        final FetchResponse.Envelope.Address[] fromAddresses = buildAddresses(headers, ImapConstants.RFC822_FROM);
        final FetchResponse.Envelope.Address[] senderAddresses = buildAddresses(headers, ImapConstants.RFC822_SENDER, fromAddresses);
        final FetchResponse.Envelope.Address[] replyToAddresses = buildAddresses(headers, ImapConstants.RFC822_REPLY_TO, fromAddresses);
        final FetchResponse.Envelope.Address[] toAddresses = buildAddresses(headers, ImapConstants.RFC822_TO);
        final FetchResponse.Envelope.Address[] ccAddresses = buildAddresses(headers, ImapConstants.RFC822_CC);
        final FetchResponse.Envelope.Address[] bccAddresses = buildAddresses(headers, ImapConstants.RFC822_BCC);
        final String inReplyTo = headerValue(headers, ImapConstants.RFC822_IN_REPLY_TO);
        final String messageId = headerValue(headers, ImapConstants.RFC822_MESSAGE_ID);
        return new EnvelopeImpl(date, subject, fromAddresses, senderAddresses, replyToAddresses, toAddresses, ccAddresses, bccAddresses, inReplyTo, messageId);
    }

    private String headerValue(Headers message, String headerName) {
        final MessageResult.Header header = MessageResultUtils.getMatching(headerName, message.headers());
        final String result;
        if (header == null) {
            result = null;
        } else {
            final String value = header.getValue();
            if (value == null || "".equals(value)) {
                result = null;
            } else {

                // ENVELOPE header values must be unfolded
                // See IMAP-269
                //
                //
                // IMAP-Servers are advised to also replace tabs with single spaces while doing the unfolding. This is what javamails
                // unfold does. mime4j's unfold does strictly follow the rfc and so preserve them
                //
                // See IMAP-327 and https://mailman2.u.washington.edu/mailman/htdig/imap-protocol/2010-July/001271.html
                result = MimeUtility.unfold(value);

            }
        }
        return result;
    }

    private FetchResponse.Envelope.Address[] buildAddresses(Headers message, String headerName, FetchResponse.Envelope.Address[] defaults) {
        final FetchResponse.Envelope.Address[] results;
        final FetchResponse.Envelope.Address[] addresses = buildAddresses(message, headerName);
        if (addresses == null) {
            results = defaults;
        } else {
            results = addresses;
        }
        return results;
    }

    private FetchResponse.Envelope.Address[] buildAddresses(Headers message, String headerName) {
        final MessageResult.Header header = MessageResultUtils.getMatching(headerName, message.headers());
        FetchResponse.Envelope.Address[] results;
        if (header == null) {
            results = null;
        } else {

            // We need to unfold the header line.
            // See https://issues.apache.org/jira/browse/IMAP-154
            //
            // IMAP-Servers are advised to also replace tabs with single spaces while doing the unfolding. This is what javamails
            // unfold does. mime4j's unfold does strictly follow the rfc and so preserve them
            //
            // See IMAP-327 and https://mailman2.u.washington.edu/mailman/htdig/imap-protocol/2010-July/001271.html
            String value = MimeUtility.unfold(header.getValue());

            if ("".equals(value.trim())) {
                results = null;
            } else {

                AddressList addressList = LenientAddressParser.DEFAULT.parseAddressList(value);
                final int size = addressList.size();
                final List<FetchResponse.Envelope.Address> addresses = new ArrayList<>(size);
                for (Address address : addressList) {
                    if (address instanceof Group) {
                        final Group group = (Group) address;
                        addAddresses(group, addresses);

                    } else if (address instanceof Mailbox) {
                        final Mailbox mailbox = (Mailbox) address;
                        final FetchResponse.Envelope.Address mailboxAddress = buildMailboxAddress(mailbox);
                        addresses.add(mailboxAddress);

                    } else {
                        log.warn("Unknown address type {}", address.getClass());
                    }
                }

                results = addresses.toArray(FetchResponse.Envelope.Address.EMPTY);
                

            }
        }
        return results;
    }

    private FetchResponse.Envelope.Address buildMailboxAddress(org.apache.james.mime4j.dom.address.Mailbox mailbox) {
        // Encode the mailbox name
        // See IMAP-266
        String name = mailbox.getName();
        if (name != null) {
            name = EncoderUtil.encodeAddressDisplayName(name);
        }

        final String domain = mailbox.getDomain();
        final DomainList route = mailbox.getRoute();
        final String atDomainList;
        if (route == null || route.isEmpty()) {
            atDomainList = null;
        } else {
            atDomainList = route.toRouteString();
        }
        final String localPart = mailbox.getLocalPart();
        return buildMailboxAddress(name, atDomainList, localPart, domain);
    }

    private void addAddresses(Group group, List<FetchResponse.Envelope.Address> addresses) {
        final String groupName = group.getName();
        final FetchResponse.Envelope.Address start = startGroup(groupName);
        addresses.add(start);
        final MailboxList mailboxList = group.getMailboxes();
        for (Mailbox mailbox : mailboxList) {
            final FetchResponse.Envelope.Address address = buildMailboxAddress(mailbox);
            addresses.add(address);
        }
        final FetchResponse.Envelope.Address end = endGroup();
        addresses.add(end);
    }

    private FetchResponse.Envelope.Address startGroup(String groupName) {
        return new AddressImpl(null, null, groupName, null);
    }

    private FetchResponse.Envelope.Address endGroup() {
        return new AddressImpl(null, null, null, null);
    }

    private FetchResponse.Envelope.Address buildMailboxAddress(String name, String atDomainList, String mailbox, String domain) {
        return new AddressImpl(atDomainList, domain, mailbox, name);
    }
}