# Quick Start Guide - Tic-Tac-Toe Multi-Client Server

## 60-Second Setup

### Option 1: Using Compiled Classes (Fastest)
```bash
cd tttnew

# Terminal 1 - Start Server
java -cp target/classes tictactoe_new.Server

# Terminal 2 - Client 1
java -cp target/classes tictactoe_new.Client

# Terminal 3 - Client 2 (will wait)
java -cp target/classes tictactoe_new.Client
```

### Option 2: Using Pre-Built JAR
```bash
cd tttnew

# Terminal 1 - Start Server
java -cp target/tttnew-1.0-SNAPSHOT.jar tictactoe_new.Server

# Terminal 2 - Client 1
java -cp target/tttnew-1.0-SNAPSHOT.jar tictactoe_new.Client

# Terminal 3 - Client 2
java -cp target/tttnew-1.0-SNAPSHOT.jar tictactoe_new.Client
```

## What You'll See

### Server (Terminal 1)
```
Server listening on port 12345
Waiting for client connections...

Client /127.0.0.1:50123 is now playing.
Client /127.0.0.1:50124 is waiting. Queue size: 1
Message from /127.0.0.1:50123: 5
Message from /127.0.0.1:50123: 1
...
```

### Client 1 (Terminal 2) - Playing First
```
Connected to server at localhost:12345

Welcome! You are now playing.
 1 | 2 | 3
-----------
 4 | 5 | 6
-----------
 7 | 8 | 9

Your turn. Enter cell number (1-9), 'board' to view, 'reset' for new game, or 'quit' to exit.

Your move: 5
```

### Client 2 (Terminal 3) - Waiting
```
Connected to server at localhost:12345

Welcome! A game is currently in progress.
Please wait for your turn...

(After Client 1 finishes, Client 2 sees:)
Your turn now! Game reset.
 1 | 2 | 3
-----------
 4 | 5 | 6
-----------
 7 | 8 | 9
Your turn
```

## Key Features to Observe

✅ **Multiple Clients Connect**: All three clients connect simultaneously
✅ **Only One Plays**: Only Client 1 plays initially
✅ **Queue Management**: Clients 2+ wait in queue
✅ **Automatic Promotion**: When Client 1 finishes, Client 2 automatically gets promoted
✅ **Server Persistent**: Server keeps running, ready for more games
✅ **No Thread Crashes**: Everything runs smoothly without thread management

## Build from Source

```bash
# Build
mvn clean compile

# Run tests
mvn test

# Package JAR
mvn package
```

## Common Commands

### Playing
- Enter `1` to `9` for cell position
- Type `board` to see current board
- Type `reset` after game ends to start new game
- Type `quit` to disconnect

### Custom Server/Port
```bash
java -cp target/classes tictactoe_new.Client 192.168.1.100 12345
```

## Architecture Summary

| Feature | Implementation |
|---------|-----------------|
| **Multi-Client** | NIO Selector (non-blocking) |
| **No Threads** | Single event loop |
| **Queue** | LinkedQueue FIFO |
| **Persistence** | Server stays running |
| **Scalability** | Handles thousands of connections |

## Files Overview

- `README.md` - Full documentation
- `ARCHITECTURE.md` - Technical deep dive
- `demo.sh` / `demo.bat` - Automated demos
- `src/main/java/tictactoe_new/Server.java` - Main server
- `src/main/java/tictactoe_new/Client.java` - Interactive client

## Troubleshooting

### "Connection refused"
- Ensure server is running on Port 12345
- Check firewall if connecting remotely

### "Invalid cell number"
- Cells are numbered 1-9
- Can't place marker on occupied cell

### "Please wait"
- Another player is currently playing
- Your turn will come when they finish

### "Game is over"
- Type 'reset' to start new game
- Or 'quit' to disconnect

## Testing Multi-Client (Automated)

### Windows
```bash
cd tttnew
demo.bat
```

### Linux/Mac
```bash
cd tttnew
bash demo.sh
```

---

**That's it!** You now have a fully functional multi-client Tic-Tac-Toe server. 🎮
