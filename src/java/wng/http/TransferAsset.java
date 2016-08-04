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
import wng.Asset;
import wng.Attachment;
import wng.WngException;
import org.json.simple.JSONStreamAware;

import javax.servlet.http.HttpServletRequest;

import static wng.http.JSONResponses.NOT_ENOUGH_ASSETS;

public final class TransferAsset extends CreateTransaction {

    static final TransferAsset instance = new TransferAsset();

    private TransferAsset() {
        super(new APITag[] {APITag.AE, APITag.CREATE_TRANSACTION}, "recipient", "asset", "quantityQNT");
    }

    @Override
    protected JSONStreamAware processRequest(HttpServletRequest req) throws WngException {

        long recipient = ParameterParser.getAccountId(req, "recipient", true);

        Asset asset = ParameterParser.getAsset(req);
        long quantityQNT = ParameterParser.getQuantityQNT(req);
        Account account = ParameterParser.getSenderAccount(req);

        Attachment attachment = new Attachment.ColoredCoinsAssetTransfer(asset.getId(), quantityQNT);
        try {
            return createTransaction(req, account, recipient, 0, attachment);
        } catch (WngException.InsufficientBalanceException e) {
            return NOT_ENOUGH_ASSETS;
        }
    }

}
