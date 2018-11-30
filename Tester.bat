javac *.java

set mypath=%cd%

echo off del "%mypath%\*.VAL"
del "%mypath%\*.BT"
del "%mypath%\*.TXT"

java Tester >"%mypath%\input.txt"
echo off > "%mypath%\output.txt"

java CS110_Project1 data.bt data.val < input.txt > "%mypath%\output.txt"

pause