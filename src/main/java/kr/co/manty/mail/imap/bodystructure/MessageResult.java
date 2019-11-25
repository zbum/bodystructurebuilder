/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package kr.co.manty.mail.imap.bodystructure;

import javax.mail.MessagingException;


/**
 * <p>
 * Used to get specific informations about a Message without dealing with a
 * MimeMessage instance. Demanded information can be requested by binary
 * combining the constants.
 * </p>
 * 
 * <p>
 * I came to the Idea of the MessageResult because there are many possible
 * combinations of different requests (uid, msn, MimeMessage, Flags).
 * </p>
 * <p>
 * e.g. I want to have all uids, msns and flags of all messages. (a common IMAP
 * operation) Javamail would do it that way:
 * <ol>
 * <li>get all Message objects (Message[])</li>
 * <li>call Message.getMessageNumber()</li>
 * <li>call Message.getFlags()</li>
 * <li>call Folder.getUid(Message)</li>
 * </ol>
 * <p>
 * This means creating a lazy-loading MimeMessage instance. </br> So why don't
 * call getMessages(MessageResult.UID | MessageResult.MSN |
 * MessageResult.FLAGS)? This would leave a lot of room for the implementation
 * to optimize
 * </p>
 */

public interface MessageResult extends Comparable<MessageResult> {
    /**
     * A header.
     */
    interface Header extends Content {

        /**
         * Gets the name of this header.
         * 
         * @return name of this header
         * @throws MessagingException
         */
        String getName();

        /**
         * Gets the (unparsed) value of this header.
         * 
         * @return value of this header
         * @throws MessagingException
         */
        String getValue();
    }


}
