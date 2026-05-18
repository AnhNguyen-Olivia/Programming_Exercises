# TODO

Player and board should be abstract classes. Thus we can create a 2d board or 1d or whatever we want without changing the origin board class.

fix index[] to index only as it is unnecessary to have it as an array when we are only using one index for the board.

restudy OOP def and reasond and throughly understand it.

java -cp target/classes tictactoe_new.MainGame 1 1d

mvn test

mvn -Dtest=InteractiveGameTest test

or fully-qualified:

mvn -Dtest=tictactoe_new.InteractiveGameTest test

mvn -Dtest=InteractiveGameTest#testMethodName test

javac -cp src/main/java -sourcepath src/main/java src/main/java/tictactoe_new/Server.java
java -cp src/main/java tictactoe_new.Server

javac -cp src/main/java -sourcepath src/main/java src/main/java/tictactoe_new/Client.java
java -cp src/main/java tictactoe_new.Client

java -cp target/tttnew-1.0-SNAPSHOT.jar tictactoe_new.Server

java -cp target/tttnew-1.0-SNAPSHOT.jar tictactoe_new.Client

