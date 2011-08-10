#!/bin/bash
cp local/hornet.sh target/hornet-1.0-SNAPSHOT-standalone/bin/

./target/hornet-1.0-SNAPSHOT-standalone/bin/hornet.sh $@
