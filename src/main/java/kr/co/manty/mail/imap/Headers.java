package kr.co.manty.mail.imap;

import kr.co.manty.mail.imap.bodystructure.BodyStructureBuildException;

import java.util.Iterator;

public interface Headers extends Content {
    Iterator<Header> headers() throws BodyStructureBuildException;
}
