package kr.co.manty.mail.imap.envelope;


final class EnvelopeImpl implements Envelope {

    private final Address[] bcc;

    private final Address[] cc;

    private final String date;

    private final Address[] from;

    private final String inReplyTo;

    private final String messageId;

    private final Address[] replyTo;

    private final Address[] sender;

    private final String subject;

    private final Address[] to;

    public EnvelopeImpl(String date, String subject, Address[] from, Address[] sender, Address[] replyTo, Address[] to, Address[] cc, Address[] bcc, String inReplyTo, String messageId) {
        super();
        this.bcc = bcc;
        this.cc = cc;
        this.date = date;
        this.from = from;
        this.inReplyTo = inReplyTo;
        this.messageId = messageId;
        this.replyTo = replyTo;
        this.sender = sender;
        this.subject = subject;
        this.to = to;
    }

    @Override
    public Address[] getBcc() {
        return bcc;
    }

    @Override
    public Address[] getCc() {
        return cc;
    }

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public Address[] getFrom() {
        return from;
    }

    @Override
    public String getInReplyTo() {
        return inReplyTo;
    }

    @Override
    public String getMessageId() {
        return messageId;
    }

    @Override
    public Address[] getReplyTo() {
        return replyTo;
    }

    @Override
    public Address[] getSender() {
        return sender;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public Address[] getTo() {
        return to;
    }
}