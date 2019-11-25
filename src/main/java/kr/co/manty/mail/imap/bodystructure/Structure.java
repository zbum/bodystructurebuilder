package kr.co.manty.mail.imap.bodystructure;

import kr.co.manty.mail.imap.envelope.Envelope;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Describes the message structure.
 */
interface Structure {
    /**
     * Gets the MIME media type.
     *
     * @return media type, or null if default
     */
    String getMediaType();

    /**
     * Gets the MIME content subtype
     *
     * @return subtype of null if default
     */
    String getSubType();

    /**
     * Gets body type parameters.
     *
     * @return parameters, or null
     */
    List<String> getParameters();

    /**
     * Gets <code>Content-ID</code>.
     *
     * @return MIME content ID, possibly null
     */
    String getId();

    /**
     * Gets <code>Content-Description</code>.
     *
     * @return MIME <code>Content-Description</code>, possibly null
     */
    String getDescription();

    /**
     * Gets content transfer encoding.
     *
     * @return MIME <code>Content-Transfer-Encoding</code>, possibly null
     */
    String getEncoding();

    /**
     * Gets the size of message body the in octets.
     *
     * @return number of octets in the message.
     */
    long getOctets();

    /**
     * Gets the number of lines fo transfer encoding for a <code>TEXT</code>
     * type.
     *
     * @return number of lines when <code>TEXT</code>, -1 otherwise
     */
    long getLines();

    /**
     * Gets <code>Content-MD5</code>.
     *
     * @return Content-MD5 or null if <code>BODY</code> FETCH or not present
     */
    String getMD5();

    /**
     * Gets header field-value from <code>Content-Disposition</code>.
     *
     * @return map of field value <code>String</code> indexed by field name
     *         <code>String</code> or null if <code>BODY</code> FETCH or not
     *         present
     */
    Map<String, String> getDispositionParams();

    /**
     * Gets header field-value from <code>Content-Disposition</code>.
     *
     * @return disposition or null if <code>BODY</code> FETCH or not present
     */
    String getDisposition();

    /**
     * Gets MIME <code>Content-Language</code>'s.
     *
     * @return List of <code>Content-Language</code> name
     *         <code>String</code>'s possibly null or null when
     *         <code>BODY</code> FETCH
     */
    List<String> getLanguages();

    /**
     * Gets <code>Content-Location</code>.
     *
     * @return Content-Location possibly null; or null when
     *         <code>BODY</code> FETCH
     */
    String getLocation();

    /**
     * Iterates parts of a composite media type.
     *
     * @return <code>Structure</code> <code>Iterator</code> when composite
     *         type, null otherwise
     */
    Iterator<Structure> parts();

    /**
     * Gets the envelope of an embedded mail.
     *
     * @return <code>Envelope</code> when <code>message/rfc822</code>
     *         otherwise null
     */
    Envelope getEnvelope();

    /**
     * Gets the envelope of an embedded mail.
     *
     * @return <code>Structure</code> when when <code>message/rfc822</code>
     *         otherwise null
     */
    Structure getBody();
}
