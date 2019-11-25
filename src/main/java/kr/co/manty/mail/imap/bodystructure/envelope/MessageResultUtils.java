package kr.co.manty.mail.imap.bodystructure.envelope;

import kr.co.manty.mail.imap.bodystructure.MessageResult;

import javax.mail.MessagingException;
import java.util.*;

public class MessageResultUtils {

    /**
     * Gets all header lines.
     * 
     * @param iterator
     *            {@link org.apache.james.mailbox.MessageResult.Header} <code>Iterator</code>
     * @return <code>List</code> of <code>MessageResult.Header<code>'s,
     * in their natural order
     * 
     * @throws MessagingException
     */
    public static List<MessageResult.Header> getAll(Iterator<MessageResult.Header> iterator) {
        final List<MessageResult.Header> results = new ArrayList<>();
        if (iterator != null) {
            while (iterator.hasNext()) {
                results.add(iterator.next());
            }
        }
        return results;
    }

    /**
     * Gets header lines whose header names matches (ignoring case) any of those
     * given.
     * 
     * @param names
     *            header names to be matched, not null
     * @param iterator
     *            {@link org.apache.james.mailbox.MessageResult.Header} <code>Iterator</code>
     * @return <code>List</code> of <code>MessageResult.Header</code>'s, in
     *         their natural order
     * @throws MessagingException
     */
    public static List<MessageResult.Header> getMatching(String[] names, Iterator<MessageResult.Header> iterator) {
        final List<MessageResult.Header> results = new ArrayList<>(20);
        if (iterator != null) {
            while (iterator.hasNext()) {
                MessageResult.Header header = iterator.next();
                final String headerName = header.getName();
                if (headerName != null) {
                    if (Arrays.stream(names)
                        .anyMatch(headerName::equalsIgnoreCase)) {
                        results.add(header);
                    }
                }
            }
        }
        return results;
    }

    /**
     * Gets header lines whose header names matches (ignoring case) any of those
     * given.
     *
     * @param names
     *            header names to be matched, not null
     * @param iterator
     *            {@link org.apache.james.mailbox.MessageResult.Header} <code>Iterator</code>
     * @return <code>List</code> of <code>MessageResult.Header</code>'s, in
     *         their natural order
     * @throws MessagingException
     */
    public static List<MessageResult.Header> getMatching(Collection<String> names, Iterator<MessageResult.Header> iterator)  {
        return matching(names, iterator, false);
    }

    private static List<MessageResult.Header> matching(Collection<String> names, Iterator<MessageResult.Header> iterator, boolean not) {
        final List<MessageResult.Header> results = new ArrayList<>(names.size());
        if (iterator != null) {
            while (iterator.hasNext()) {
                final MessageResult.Header header = iterator.next();
                final boolean match = contains(names, header);
                final boolean add = (not && !match) || (!not && match);
                if (add) {
                    results.add(header);
                }
            }
        }
        return results;
    }

    private static boolean contains(Collection<String> names, MessageResult.Header header)  {
        final String headerName = header.getName();
        if (headerName != null) {
            return names.stream().anyMatch(name -> name.equalsIgnoreCase(headerName));
        }
        return false;
    }

    /**
     * Gets header lines whose header names matches (ignoring case) any of those
     * given.
     * 
     * @param names
     *            header names to be matched, not null
     * @param iterator
     *            {@link org.apache.james.mailbox.MessageResult.Header} <code>Iterator</code>
     * @return <code>List</code> of <code>MessageResult.Header</code>'s, in
     *         their natural order
     * @throws MessagingException
     */
    public static List<MessageResult.Header> getNotMatching(Collection<String> names, Iterator<MessageResult.Header> iterator)  {
        return matching(names, iterator, true);
    }

    /**
     * Gets a header matching the given name. The matching is case-insensitive.
     * 
     * @param name
     *            name to be matched, not null
     * @param iterator
     *            <code>Iterator</code> of <code>MessageResult.Header</code>'s,
     *            not null
     * @return <code>MessageResult.Header</code>, or null if the header does not
     *         exist
     * @throws MessagingException
     */
    public static MessageResult.Header getMatching(String name, Iterator<MessageResult.Header> iterator) {
        MessageResult.Header result = null;
        if (name != null) {
            while (iterator.hasNext()) {
                MessageResult.Header header = iterator.next();
                final String headerName = header.getName();
                if (name.equalsIgnoreCase(headerName)) {
                    result = header;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Gets header lines whose header name fails to match (ignoring case) all of
     * the given names.
     * 
     * @param names
     *            header names, not null
     * @param iterator
     *            {@link org.apache.james.mailbox.MessageResult.Header} <code>Iterator</code>
     * @return <code>List</code> of <code>@MessageResult.Header</code>'s, in
     *         their natural order
     * @throws MessagingException
     */
    public static List<MessageResult.Header> getNotMatching(String[] names, Iterator<MessageResult.Header> iterator)  {
        final List<MessageResult.Header> results = new ArrayList<>(20);
        if (iterator != null) {
            while (iterator.hasNext()) {
                MessageResult.Header header = iterator.next();
                final String headerName = header.getName();
                if (headerName != null) {
                    boolean match = Arrays.stream(names)
                        .anyMatch(headerName::equalsIgnoreCase);
                    if (!match) {
                        results.add(header);
                    }
                }
            }
        }
        return results;
    }
}
