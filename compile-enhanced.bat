@echo off
echo   Compiling Ludo Master with Chat

REM Clean and create bin directory
if exist bin rmdir /s /q bin
mkdir bin

REM Compile all Java files
javac -d bin -sourcepath src src\Main.java src\client\*.java src\model\*.java src\server\*.java src\util\*.java

if %errorlevel% equ 0 (
    echo   Compilation Successful!
) else (
    echo   Compilation Failed!
)

pause
