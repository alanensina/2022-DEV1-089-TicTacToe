# Kata challenge: Tic Tac Toe

## What do you need to run the project?
- Java 11+
- Maven
- Your favorite IDE

## How to run?
- Import the project into your IDE;
- Install the Maven dependencies;
- Run the project.

## Rules
- X always goes first.
- Players cannot play on a played position.
- Players alternate placing X’s and O’s on the board until either:
    - One player has three in a row, horizontally, vertically or diagonally
    - All nine squares are filled.
- If a player is able to draw three X’s or three O’s in a row, that player wins.
- If all nine squares are filled and neither player has three in a row, the game is a draw.

## How to play?
### Before to start:
- Please take a note that every position in the board has an index, so, before to make a move, check the index that you want to choose:
### ![alt text](https://i.imgur.com/9olpowi.png)
- Example: if you want to put a X on the first cell, select the index 0.
- So, to win the game, you must put a X or a O three in a row, horizontally, vertically or diagonally:
    - 0 1 2 **or** 3 4 5 **or** 6 7 8 (horizontally)
    - 0 3 6 **or** 1 4 7 **or** 2 5 8 (vertically)
    - 0 4 8 **or** 2 4 6 (diagonally)


- There are 4 endpoints available on this API:
    - **/start** <font color="orange"> (POST) </font>: Used to start a game, the ID on response will be used, so save it.
    - **/list** <font color="green"> (GET) </font>: List all games stored on memory
    - **/find/{id}** <font color="green"> (GET) </font>: Find a specific game by his ID
    - **/play** <font color="blue"> (PUT) </font>: Used to make the moves.

### Playing:
- Start the game sending a request to the first endpoint (/start) and save the ID on the response.
- Send a request to the fourth endpoint (/play) with the following information:
```
{
    "id": id of the game,
    "player": player that will make a move (Valid inputs: "X" or "O"),
    "position": index of the position that you want to make a move (Valid inputs: 0, 1, 2, 3, 4, 5, 6, 7 and 8)
}
```
- Check an example:
```
{
    "id": 1,
    "player": "X",
    "position": 2
}
```
- As response you will get an information of the game:
```
{
    "games": [
        {
            "id": 1,
            "nextPlayer": "O",
            "positions": [
                null,
                null,
                "X",
                null,
                null,
                null,
                null,
                null,
                null
            ],
            "finished": false
        }
    ]
}
```
- Checking the JSON above, it's possible to check that the next move is the player "O" and he can't play in the same position (2) that "X" played.

- Moves must be interspersed between X and O
- Make as many moves as necessary to win the game or tie it.
- To help you to play, check the Postman collection below:

https://www.getpostman.com/collections/550dd4a709578d09a645