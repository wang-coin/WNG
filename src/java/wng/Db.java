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

package wng;

import wng.db.BasicDb;
import wng.db.TransactionalDb;

public final class Db {

    public static final String PREFIX = Constants.isTestnet ? "wng.testDb" : "wng.db";
    public static final TransactionalDb db = new TransactionalDb(new BasicDb.DbProperties()
            .maxCacheSize(Wng.getIntProperty("wng.dbCacheKB"))
            .dbUrl(Wng.getStringProperty(PREFIX + "Url"))
            .dbType(Wng.getStringProperty(PREFIX + "Type"))
            .dbDir(Wng.getStringProperty(PREFIX + "Dir"))
            .dbParams(Wng.getStringProperty(PREFIX + "Params"))
            .dbUsername(Wng.getStringProperty(PREFIX + "Username"))
            .dbPassword(Wng.getStringProperty(PREFIX + "Password", null, true))
            .maxConnections(Wng.getIntProperty("wng.maxDbConnections"))
            .loginTimeout(Wng.getIntProperty("wng.dbLoginTimeout"))
            .defaultLockTimeout(Wng.getIntProperty("wng.dbDefaultLockTimeout") * 1000)
    );

    static void init() {
        db.init(new WngDbVersion());
    }

    static void shutdown() {
        db.shutdown();
    }

    private Db() {} // never

}
