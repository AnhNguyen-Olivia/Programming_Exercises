@echo off
REM Demo script for multi-client Tic-Tac-Toe server (Windows)
REM This demonstrates the single-user, multi-client queuing system

echo.
echo ======================================
echo Tic-Tac-Toe Multi-Client Demo
echo ======================================
echo.

REM Compile the project
echo [1/3] Compiling project...
call mvn clean compile -q
if errorlevel 1 (
    echo Compilation failed!
    exit /b 1
)
echo. Compilation successful
echo.

REM Start the server in background
echo [2/3] Starting server on port 12345...
start "Server" java -cp target\classes tictactoe_new.Server
timeout /t 2 /nobreak > nul
echo. Server started
echo.

echo [3/3] Starting multiple clients...
echo.
echo INSTRUCTIONS:
echo ==============
echo - Client 1 will open in first window (will play immediately)
echo   Try moves: 5, then 1
echo.
echo - After Client 1 finishes, Client 2 will play
echo   Try moves: 5, then 3
echo.
echo - Then Client 3 will play
echo   Try moves: 5, then 7
echo.
echo Type 'quit' or 'reset' then 'quit' when done.
echo.

echo Opening Client 1...
timeout /t 2 /nobreak > nul
start "Client 1" java -cp target\classes tictactoe_new.Client localhost 12345

echo Opening Client 2 (will wait)...
timeout /t 3 /nobreak > nul
start "Client 2" java -cp target\classes tictactoe_new.Client localhost 12345

echo Opening Client 3 (will wait)...
timeout /t 3 /nobreak > nul
start "Client 3" java -cp target\classes tictactoe_new.Client localhost 12345

echo.
echo ======================================
echo Demo is running in separate windows
echo ======================================
echo.
echo Watch the Server window to see:
echo - Client connection order
echo - Queue management
echo - Player transitions
echo.
pause
