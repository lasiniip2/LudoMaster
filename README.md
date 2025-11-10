# LudoMaster
# 🎲 Ludo Network Game (Java Sockets + Multithreading)

A multiplayer **Ludo Game** built using **Java Network Programming concepts**, including:
- TCP Sockets  
- Object Streams  
- Multithreading  
- Client-Server Communication  
- Basic Swing GUI for the Board  

This project was developed for the *Network Programming Assignment* — demonstrating Java NIO, concurrency, and client-server communication.

---

## 🧱 Project Structure

LudoNetworkGame/
│
├── src/
│ ├── client/
│ │ ├── LudoClient.java
│ │ ├── LudoBoardUI.java
│ │
│ ├── server/
│ │ ├── LudoServer.java
│ │ ├── ClientHandler.java
│ │ ├── GameManager.java
│ │
│ ├── model/
│ │ ├── Player.java
│ │ ├── BotPlayer.java
│ │ ├── GameState.java
│ │ ├── Message.java
│ │
│ ├── util/
│ │ ├── Constants.java
│ │
│ └── Main.java
│
├── bin/ # ← Compiled .class files (ignored by Git)
├── README.md
└── .gitignore


---

## 🧩 Features

- 🎯 **Multiplayer Support:** 1–4 players (local or across LAN)
- ⚙️ **Multithreaded Server:** Handles each player on a separate thread  
- 💬 **Client Communication:** Real-time updates via Object Streams  
- 🧠 **Bot Players:** Automatically added when fewer than 4 players  
- 🎨 **Simple Swing UI:** Visual board with dice and player updates  
- 🧵 Demonstrates: `Socket`, `ServerSocket`, `Thread`, `ObjectInputStream`, `ObjectOutputStream`

---

## ⚙️ Setup Instructions

### 1️⃣ Clone the repository

```bash
clone the repo 
cd LudoMaster (root folder)

2️⃣ Compile the project

From your project root (where /src exists):

javac -d bin (Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName })

--------------------------------------------------------------------
3️⃣ Run the server

In one terminal:

cd bin
java server.LudoServer

You’ll see:

=== LUDO SERVER STARTED ===
Enter number of players (1–4):
Then enter 1, 2, 3, or 4.

4️⃣ Run the clients

In separate terminals (or separate computers on LAN):

cd bin
java client.LudoClient


Each client will be asked:

Enter your player name:

🎮 Gameplay Modes
Mode	Description	Example
1 Player	You play against 3 server bots	1 client connected
2 Players	Two human players, two bots	2 clients connected
3 Players	Three humans, one bot	3 clients connected
4 Players	Full match — four human players	4 clients connected

How It Works

The Server starts and waits for N players (1–4).

Each Client connects using sockets and sends a JOIN message.

The server starts the game automatically when all expected players join.

The GameManager handles:

Current turn rotation

Dice rolls and player movements

Broadcasting state to all clients

Clients update their boards in real time via ObjectInputStream events.

🧩 Commands

🎲 Roll Dice: Press the button when it’s your turn.

🏁 Game Ends: When all players finish (basic logic).
-------------------------------------------------------------------
🧑‍💻 Contributors (for assignment)
Name	Role	Contribution
Member 1	Server logic	LudoServer, GameManager
Member 2	Client networking	LudoClient
Member 3	GUI	LudoBoardUI
Member 4	Model + Bots	GameState, BotPlayer
Member 5	Integration + Testing	Full setup & documentation
🏁 Example Run

Terminal 1 (Server):

=== LUDO SERVER STARTED ===
Enter number of players (1–4): 2
Waiting for 2 player(s) to connect...
Player connected from: /127.0.0.1
[SERVER] Player joined: Thilshath
Player connected from: /127.0.0.1
[SERVER] Player joined: Lashini
Game Started with 4 players!


Terminal 2 & 3 (Clients):

Enter your player name: Michel
Your turn! Click 🎲 Roll Dice


When a player disconnects:

Michel has left the game.

