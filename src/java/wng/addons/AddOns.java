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

package wng.addons;

import wng.Wng;
import wng.http.APIServlet;
import wng.http.APITag;
import wng.util.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class AddOns {

    private static final List<AddOn> addOns;
    static {
        List<AddOn> addOnsList = new ArrayList<>();
        Wng.getStringListProperty("wng.addOns").forEach(addOn -> {
            try {
                addOnsList.add((AddOn)Class.forName(addOn).newInstance());
            } catch (ReflectiveOperationException e) {
                Logger.logErrorMessage(e.getMessage(), e);
            }
        });
        addOns = Collections.unmodifiableList(addOnsList);
        if (!addOns.isEmpty()) {
            System.setProperty("java.security.policy", Wng.isDesktopApplicationEnabled() ? "wngdesktop.policy" : "wng.policy");
            Logger.logMessage("Setting security manager with policy " + System.getProperty("java.security.policy"));
            System.setSecurityManager(new SecurityManager());
        }
        addOns.forEach(addOn -> {
            Logger.logInfoMessage("Initializing " + addOn.getClass().getName());
            addOn.init();
        });
    }

    public static void init() {}

    public static void shutdown() {
        addOns.forEach(addOn -> {
            Logger.logShutdownMessage("Shutting down " + addOn.getClass().getName());
            addOn.shutdown();
        });
    }

    public static void registerAPIRequestHandlers(Map<String,APIServlet.APIRequestHandler> map) {
        for (AddOn addOn : addOns) {
            APIServlet.APIRequestHandler requestHandler = addOn.getAPIRequestHandler();
            if (requestHandler != null) {
                if (!requestHandler.getAPITags().contains(APITag.ADDONS)) {
                    Logger.logErrorMessage("Add-on " + addOn.getClass().getName()
                            + " attempted to register request handler which is not tagged as APITag.ADDONS, skipping");
                    continue;
                }
                String requestType = addOn.getAPIRequestType();
                if (requestType == null) {
                    Logger.logErrorMessage("Add-on " + addOn.getClass().getName() + " requestType not defined");
                    continue;
                }
                if (map.get(requestType) != null) {
                    Logger.logErrorMessage("Add-on " + addOn.getClass().getName() + " attempted to override requestType " + requestType + ", skipping");
                    continue;
                }
                Logger.logMessage("Add-on " + addOn.getClass().getName() + " registered new API: " + requestType);
                map.put(requestType, requestHandler);
            }
        }
    }

    private AddOns() {}

}
