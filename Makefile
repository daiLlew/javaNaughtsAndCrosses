.PHONY: game
game:
	mvn clean package
	java -jar target/tic-tac-toe-1.0-SNAPSHOT.jar