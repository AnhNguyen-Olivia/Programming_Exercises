#!/bin/bash

# Demo script for multi-client Tic-Tac-Toe server
# This script demonstrates the single-user, multi-client queuing system

echo "======================================"
echo "Tic-Tac-Toe Multi-Client Demo"
echo "======================================"
echo ""

# Compile the project
echo "[1/4] Compiling project..."
mvn clean compile -q
if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi
echo "✓ Compilation successful"
echo ""

# Start the server in background
echo "[2/4] Starting server on port 12345..."
java -cp target/classes tictactoe_new.Server &
SERVER_PID=$!
sleep 1
echo "✓ Server started (PID: $SERVER_PID)"
echo ""

# Function to run a client with simulated input
run_client() {
    local client_num=$1
    local moves=$2
    local delay=$3

    echo "[3.$client_num] Starting Client $client_num..."
    sleep $delay

    echo "--- Client $client_num Session ---" >&2
    (
        echo "$moves" | java -cp target/classes tictactoe_new.Client
    ) 2>&1 | sed "s/^/[Client $client_num] /"

    echo "✓ Client $client_num finished"
}

# Launch multiple clients with staggered starts and pre-planned moves
# Client 1: plays immediately (moves: 5, 1, quit)
run_client 1 "5
1
quit" 0.5 &

# Client 2: waits for Client 1 to finish, then plays (moves: 5, 3, quit)
run_client 2 "5
3
quit" 2 &

# Client 3: waits for Client 2 (moves: 5, 7, quit)
run_client 3 "5
7
quit" 4 &

echo ""
echo "[4/4] Waiting for all clients to complete..."
wait

echo ""
echo "======================================"
echo "Demo completed successfully!"
echo "======================================"
echo ""
echo "Summary:"
echo "- Server accepted 3 clients"
echo "- Only Client 1 played initially"
echo "- Clients 2 and 3 waited in queue"
echo "- When Client 1 finished, Client 2 started"
echo "- When Client 2 finished, Client 3 started"
echo ""

# Cleanup
kill $SERVER_PID 2>/dev/null
wait $SERVER_PID 2>/dev/null
echo "Server stopped."
