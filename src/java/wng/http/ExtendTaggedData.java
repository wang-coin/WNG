/******************************************************************************
 * Copyright © 2013-2016 The Nxt Core Developers.                             *
 *                                                                            *
 * See the AUTHORS.txt, DEVELOPER-AGREEMENT.txt and LICENSE.txt files at      *
 * the top-level directory of this distribution for the individual copyright  *
 * holder information and the developer policies on copyright and licensing.  *
 *                                                                            *
 * Unless otherwise agreed in a custom licensing agreement, no part of the    *
 * Nxt software, including this file, may be copied, modified, propagated,    *
 * or distributed except according to the terms contained in the LICENSE.txt  *
 * file.                                                                      *
 *                                                                            *
 * Removal or modification of this copyright notice is prohibited.            *
 *                                                                            *
 ******************************************************************************/

package wng.http;

import wng.Account;
import wng.Attachment;
import wng.Wng;
import wng.WngException;
import wng.TaggedData;
import wng.Transaction;
import wng.TransactionType;
import org.json.simple.JSONStreamAware;

import javax.servlet.http.HttpServletRequest;

import static wng.http.JSONResponses.UNKNOWN_TRANSACTION;

public final class ExtendTaggedData extends CreateTransaction {

    static final ExtendTaggedData instance = new ExtendTaggedData();

    private ExtendTaggedData() {
        super("file", new APITag[] {APITag.DATA, APITag.CREATE_TRANSACTION}, "transaction",
                "name", "description", "tags", "type", "channel", "isText", "filename", "data");
    }

    @Override
    protected JSONStreamAware processRequest(HttpServletRequest req) throws WngException {

        Account account = ParameterParser.getSenderAccount(req);
        long transactionId = ParameterParser.getUnsignedLong(req, "transaction", true);
        TaggedData taggedData = TaggedData.getData(transactionId);
        if (taggedData == null) {
            Transaction transaction = Wng.getBlockchain().getTransaction(transactionId);
            if (transaction == null || transaction.getType() != TransactionType.Data.TAGGED_DATA_UPLOAD) {
                return UNKNOWN_TRANSACTION;
            }
            Attachment.TaggedDataUpload taggedDataUpload = ParameterParser.getTaggedData(req);
            taggedData = new TaggedData(transaction, taggedDataUpload);
        }
        Attachment.TaggedDataExtend taggedDataExtend = new Attachment.TaggedDataExtend(taggedData);
        return createTransaction(req, account, taggedDataExtend);

    }

}
