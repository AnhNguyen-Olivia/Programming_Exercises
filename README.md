# TIC-TAC-TOE GAME

A project to learn how to be better in programming.

## Prerequisites

- Java 21 or higher
- Maven 3.6+

## How to Build

```bash
cd tttnew
mvn clean install
```

This will compile the code, run tests, and create a JAR file in the `target/` directory.

## How to Run the Game

### Basic Usage

```bash
java -jar target/tttnew-1.0-SNAPSHOT.jar <PLAYER> <BOARD_TYPE>
```

### Parameters

- **PLAYER**: Who plays first
  - `1` = Human player goes first
  - `2` = Computer player goes first
  
- **BOARD_TYPE**: Choose the board layout
  - `1d` = 1D board (linear representation)
  - `2d` = 2D board (traditional grid display)

### Examples

**Human goes first on a 2D board:**

```bash
java -jar target/tttnew-1.0-SNAPSHOT.jar 1 2d
```

**Computer goes first on a 2D board:**

```bash
java -jar target/tttnew-1.0-SNAPSHOT.jar 2 2d
```

**Human goes first on a 1D board:**

```bash
java -jar target/tttnew-1.0-SNAPSHOT.jar 1 1d
```

## How to run client server (single client-server)

```bash
# Terminal 1 server
mvn compile
java -cp target/classes tictactoe_new.SingleClientServer

# Terminal 2 client
java -cp target/classes tictactoe_new.Client
```

## How to run client server (multi thread client-sever)

```bash
# Terminal 1 server
mvn compile
java -cp target/classes tictactoe_new.multithreadingServer

# Terminal 2 client
java -cp target/classes tictactoe_new.multithreadClient
```

### Test crashing if deploy 10K user

chmod +x testCrash.sh
./testCrash.sh

## How to Play

- Enter your move as a position number (0-8 for 2D board, or as specified in the game)
- Try to get three in a row (horizontally, vertically, or diagonally)
- The game will alternate between your moves and the computer's moves
- The game ends when someone wins or the board is full (tie)

## Running Tests

```bash
mvn test
```

This will run all unit tests in the `src/test/` directory.

## Client Server Protocol

![alt text](miscellaneous/BasicProtocol.png "Basic Protocol Diagram for Client-server, single-user, single-threaded terminal-based human-computer, basic Tic-Tac-Toe")
