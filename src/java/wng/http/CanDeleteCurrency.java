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

import wng.Currency;
import wng.WngException;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;

import javax.servlet.http.HttpServletRequest;

public final class CanDeleteCurrency extends APIServlet.APIRequestHandler {

    static final CanDeleteCurrency instance = new CanDeleteCurrency();

    private CanDeleteCurrency() {
        super(new APITag[] {APITag.MS}, "account", "currency");
    }

    @Override
    protected JSONStreamAware processRequest(HttpServletRequest req) throws WngException {
        Currency currency = ParameterParser.getCurrency(req);
        long accountId = ParameterParser.getAccountId(req, true);
        JSONObject response = new JSONObject();
        response.put("canDelete", currency.canBeDeletedBy(accountId));
        return response;
    }

}
