@echo off
echo ###################################################################
echo #        This build and release script requires Apache Ant.
echo ###################################################################
echo Download ant from http://ant.apache.org/
echo Set the ANT_HOME environment variable to where you extracted Ant
echo Add Ant's bin directory to the environment path
echo ###################################################################
call ant release
pause