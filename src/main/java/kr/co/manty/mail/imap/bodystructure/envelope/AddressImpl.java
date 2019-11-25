package kr.co.manty.mail.imap.bodystructure.envelope;

import kr.co.manty.mail.imap.bodystructure.FetchResponse;

final class AddressImpl implements FetchResponse.Envelope.Address {
    private final String atDomainList;

    private final String hostName;

    private final String mailboxName;

    private final String personalName;

    public AddressImpl(String atDomainList, String hostName, String mailboxName, String personalName) {
        super();
        this.atDomainList = atDomainList;
        this.hostName = hostName;
        this.mailboxName = mailboxName;
        this.personalName = personalName;
    }

    @Override
    public String getAtDomainList() {
        return atDomainList;
    }

    @Override
    public String getHostName() {
        return hostName;
    }

    @Override
    public String getMailboxName() {
        return mailboxName;
    }

    @Override
    public String getPersonalName() {
        return personalName;
    }
}