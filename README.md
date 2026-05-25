# Tic-Tac-Toe Game with Client-Server Architecture

## Overview

This is a Java-based Tic-Tac-Toe game featuring a TCP-based server that handles multiple concurrent clients. Only one player can play against the computer at a time, while other connected clients wait in a queue.

## Architecture

### Key Design Principles

- **Single-User Gameplay**: Only one player can play against the computer at any given moment
- **Multi-Client Support**: Multiple clients can connect simultaneously, with waiting clients in a queue
- **No Threads**: Uses Java NIO (Selector) for non-blocking, multiplexed I/O without explicit threads
- **Server Persistence**: The server remains running and accepts new games continuously
- **Fair Queuing**: Clients are served in FIFO (First-In-First-Out) order

### Server Components

- `Server.java`: Main server implementation using NIO Selector for multi-client handling
  - Manages a shared game board and computer player
  - Tracks current player and queued waiting clients
  - Handles client connections/disconnections gracefully
  - Broadcasts turn changes to waiting clients

- `Computer.java`: Simple AI player (makes first available move)
- `Board2D.java`: 3x3 Tic-Tac-Toe board implementation

### Client Components

- `Client.java`: Interactive client that connects to the server
  - Handles user input prompts
  - Displays board state and game status
  - Supports commands: cell number, 'board', 'reset', 'quit'

## Building the Project

```bash
cd tttnew
mvn clean compile
```

## Running Tests

```bash
mvn test
```

## Running the Game

### Start the Server

```bash
# Option 1: Using Maven
java -cp target/classes tictactoe_new.Server

# Option 2: Using JAR
java -cp target/tttnew-1.0-SNAPSHOT.jar tictactoe_new.Server
```

Server output:

```bash
Server listening on port 12345
Waiting for client connections...
```

### Connect Clients (in separate terminals)

**First Client (will play immediately):**

```bash
java -cp target/classes tictactoe_new.Client
```

Or with custom server address:

```bash
java -cp target/classes tictactoe_new.Client 192.168.1.100 12345
```

**Second Client (will wait in queue):**

```bash
java -cp target/classes tictactoe_new.Client
```

**Third Client (will also wait):**

```bash
java -cp target/classes tictactoe_new.Client
```

## Gameplay

### Client Commands

- **1-9**: Place marker on cell number
- **board**: View current board state
- **reset**: Start a new game (only available after game ends)
- **quit**: Disconnect from server

### Example Session

**Server Console:**

```terminal
Client /127.0.0.1:50123 is now playing.
Client /127.0.0.1:50124 is waiting. Queue size: 1
Client /127.0.0.1:50125 is waiting. Queue size: 2
Message from /127.0.0.1:50123: 5
Message from /127.0.0.1:50123: 1
...
Current player disconnected. Moving to next player...
Next player started. Queue size: 1
```

**First Client:**

```terminal
Connected to server at localhost:12345

Welcome! You are now playing.
 0 | 0 | 0
 0 | 0 | 0
 0 | 0 | 0

Your turn. Enter cell number (1-9), 'board' to view, 'reset' for new game, or 'quit' to exit.

Your move: 5
 0 | 0 | 0
 0 | 1 | 0
 0 | 0 | 0

 2 | 0 | 0
 0 | 1 | 0
 0 | 0 | 0
Your turn
```

**Second Client (waiting):**

```terminal
Connected to server at localhost:12345

Welcome! A game is currently in progress.
Please wait for your turn...

(Waiting for your turn... commands: 'quit' to exit)

Your turn now! Game reset.
 0 | 0 | 0
 0 | 0 | 0
 0 | 0 | 0
Your turn
```

## Project Structure

```terminal
src/
├── main/java/tictactoe_new/
│   ├── Server.java                 (Main server with NIO Selector)
│   ├── Client.java                 (Interactive client)
│   ├── Board.java                  (Abstract board base class)
│   ├── Board2D.java                (3x3 board implementation)
│   ├── Board1D.java                (1D board implementation)
│   ├── Computer.java               (AI player)
│   ├── HumanPlayer.java            (Human player)
│   ├── GameLogic.java              (Game flow logic)
│   ├── Player.java                 (Base player class)
│   ├── Position.java               (Board position)
│   └── Constants.java              (Game constants)
├── test/java/tictactoe_new/
│   ├── BoardTest.java
│   ├── PlayerTest.java
│   ├── MainGameTest.java
│   └── InteractiveGameTest.java
└── pom.xml

target/
└── (compiled classes and JAR)
```

## Technical Details

### Multi-Client Handling (No Threads)

The server uses Java NIO's `Selector` for non-blocking, multiplexed I/O:

- Single event loop processes all client connections
- `Selector.select()` waits for I/O readiness on registered channels
- Each client socket is non-blocking and registered with the selector
- Game state is shared across all clients
- Queue implemented using `LinkedQueue<SocketChannel>`

### Game State Synchronization

- Current player tracked by `currentPlayer` (SocketChannel reference)
- Waiting clients stored in `waitingClients` queue
- When current player disconnects or finishes game, next client is promoted
- All messages are sent using UTF-8 encoding for reliability

## Known Limitations

- Computer AI is simple (plays first available cell)
- No game history or replay functionality
- Board size is fixed at 3x3
- No authentication or user sessions

## Future Enhancements

- Improved AI strategy (minimax, alpha-beta pruning)
- Support for configurable board sizes (4x4, 5x5)
- Game history and statistics tracking
- User accounts and persistence
- Spectator mode (clients can watch games)
- TCP keepalive/heartbeat to detect dead connections

## References

### Commands Reference

```bash
# Compile
mvn clean compile

# Test
mvn test
mvn -Dtest=BoardTest test

# Run server
java -cp target/classes tictactoe_new.Server

# Run client
java -cp target/classes tictactoe_new.Client
java -cp target/classes tictactoe_new.Client <hostname> <port>

# Build JAR
mvn package

# Run with JAR
java -cp target/tttnew-1.0-SNAPSHOT.jar tictactoe_new.Server
java -cp target/tttnew-1.0-SNAPSHOT.jar tictactoe_new.Client
```

## Notes on OOP Architecture

- `Board` is an abstract base class allowing multiple implementations (2D, 1D, etc.)
- `Player` is an abstract base class for both human and computer players
- `Computer` extends `Player` for AI implementation
- `HumanPlayer` extends `Player` for interactive gameplay
- `GameLogic` orchestrates game flow and win/loss detection
