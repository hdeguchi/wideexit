set CLASSES_PATH=..\..\..\..\..\classes
set LIBRARY_PATH=..\..\..\..\..\lib
set MAIN_CLASS=soars.plugin.modelbuilder.chart.live_viewer.main.Application

set CP=%CLASSES_PATH%;%LIBRARY_PATH%\chart\plot.jar;

start javaw -Xmx512m -cp %CP% %MAIN_CLASS% %*
