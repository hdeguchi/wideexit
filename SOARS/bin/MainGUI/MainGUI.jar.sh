#!/bin/sh

MAIN_CLASS=main.MainGUI
JAVA_HOME=/usr/java/j2sdk1.4.2_06

$JAVA_HOME/bin/java -cp /home/sgeadmin/workspace/SOARS/bin/MainGUI/SOARS.jar $MAIN_CLASS $@

exit 0
