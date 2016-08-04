#!/bin/sh
java -cp "classes:lib/*:conf" wng.tools.SignTransactionJSON $@
exit $?
