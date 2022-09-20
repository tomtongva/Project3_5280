# Project 3: ITIS 5280
## UNC Charlotte | Advanced Mobile Application Development
### Members:
- Alex Miller
- Tom Va
- Jared Tamulynas

## Wireframe
Find the wireframe in the Wireframe.zip file and click on the index.html file

## Link to Youtube Channel
[Project Demo]

---
## Project Purpose

In this assignment you are going to use Callable Firebase Cloud Functions and Push Notifications. The mobile app is focused on updating the UNO application to use cloud functions. The following are the user stories:

As a user, I want to be able to register with email/password, so that I can use the app. [Using Firebase]
As a user, I want to be able to login to use the app, trade and see my progress. [Using Firebase]
As a user, I want to create a new game. Use Firebase Callable Cloud Functions (FCCF) to create the game, shuffle the cards, and setup the game.
As a user, I want to join a game. Use FCCF to enable joining a game if possible and return error if the game is not available or if there is no more available player slots (only two players).
As a user, I want to draw a card. Use FCCF to manage the cards deck, and to serve a card to the user.
As a user, I want to play a card. Use FCCF to manage a card play, the cards deck, the discard pile and managing the game state.
As a user, I should be able to leave a game which makes the other player the winner of this game.
Send a push notification to all the users playing the game when a user has only one card left in their card deck (UNO).
You should use Firebase Cloud Functions and transactions to ensure atomic operations. The Firebase callable functions would include :

createNewGame()
Description: creates a new game, setup the game cards, players, and add the user creating the game as the first player.
Return gameId for the newly created game.
joinGame({gameId: "123XF4848534"}) :
Description: enables a user to join the game if the game is still pending the joining for the second player.
Returns: Success or ok if the game is joined. Returns exception if the game is not available to be joined by the new player.
drawCard({gameId: "123XF4848534"})
Description: enables the player to draw a card if it is their turn to play.
Returns: card if the player is allowed to draw a card. Returns exception if there is an error.
playCard({gameId: "123XF4848534", card:"<Depending on how you encode your card>"}))
Description: enables the player to play the provided card if it is their turn to play.
Returns: success or ok if successfully played. Returns exception if the card is not allowed, or it is not their turn, or any other error.
leaveGame({gameId: "123XF4848534"})
Description: enables the player to leave the game, which should end the game, and should make the other player the winner of this game.
Returns: success or ok if successfully executed. Returns exception if the game has already ended, or if the player is not part of the game or any other error.
Submission should include:

Create a Github or Bitbucket repo for the assignment.
Push your code to the created repo. Should contain all your code.
On the same repo create a wiki page describing your design and implementation. The wiki page should describe the cloud functions, DB Schema and all the assumptions required to provide authentication. In addition describe any data that is stored on the device or on the server
Demo your API using a mobile app that uses your implemented api.
A 5 minute (max) screencast to demo your application.