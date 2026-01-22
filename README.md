# LudoMaster - Enhanced Edition
# 🎲 Ludo Network Game with Integrated Chat (Java Sockets + Multithreading)

A multiplayer **Ludo Game** built using **Java Network Programming concepts**, now featuring a **fully integrated chat system**!

**Key Technologies:**
- TCP Sockets  
- Object Streams  
- Multithreading  
- Client-Server Communication  
- Modern Swing GUI with Dark Theme
- Real-time Chat (Group & Private Messaging)

This enhanced version combines classic Ludo gameplay with modern chat functionality, demonstrating advanced Java networking, concurrency, and GUI design.

---

## ✨ NEW: Chat Features

- **Group Chat**: Communicate with all players during the game
- **Private Messaging**: Send private messages to individual players
- **Modern UI**: WhatsApp-style message bubbles with timestamps
- **User List**: See all connected players and start conversations
- **Tabbed Interface**: Switch between group chat and private chats
- **Real-time Updates**: Messages appear instantly for all recipients

See [CHAT_FEATURES.md](CHAT_FEATURES.md) for detailed chat documentation.

---

## 🧱 Project Structure

```
LudoMaster/
│
├── src/
│   ├── client/
│   │   ├── LudoClient.java          # Client connection handler
│   │   ├── LudoBoardUI.java         # Main UI with split layout
│   │   ├── BoardPanel.java          # Enhanced game board graphics
│   │   └── ChatPanel.java           # ✨ NEW: Chat UI component
│   │
│   ├── server/
│   │   ├── LudoServer.java          # Server initialization
│   │   ├── ClientHandler.java       # Per-client thread handler
│   │   └── GameManager.java         # Game logic + chat routing
│   │
│   ├── model/
│   │   ├── Player.java              # Player entity
│   │   ├── BotPlayer.java           # AI bot player
│   │   ├── GameState.java           # Game state management
│   │   └── Message.java             # ✨ ENHANCED: Game + chat messages
│   │
│   ├── util/
│   │   └── Constants.java           # Configuration constants
│   │
│   └── Main.java                     # Entry point
│
├── bin/                              # Compiled .class files (ignored by Git)
├── README.md                         # This file
├── CHAT_FEATURES.md                  # ✨ NEW: Chat documentation
├── compile-enhanced.bat              # ✨ NEW: Build script
└── .gitignore
```

---

## 🧩 Features

### Game Features
- 🎯 **Multiplayer Support:** 1–4 players (local or across LAN)
- ⚙️ **Multithreaded Server:** Handles each player on a separate thread  
- 💬 **Real-time Updates:** Instant game state synchronization
- 🧠 **Bot Players:** Automatically added when fewer than 4 players  
- � **Turn-based Gameplay:** Classic Ludo rules with dice rolling
- 🧵 Demonstrates: `Socket`, `ServerSocket`, `Thread`, `ObjectInputStream`, `ObjectOutputStream`

### Chat Features ✨ NEW
- 💬 **Group Chat:** Broadcast messages to all players
- 🔒 **Private Messaging:** One-on-one conversations with any player
- 👥 **User List:** See all connected players
- 📱 **Modern UI:** WhatsApp-style message bubbles
- ⏰ **Timestamps:** See when each message was sent
- 🎨 **Tabbed Interface:** Separate tabs for each conversation

### UI Enhancements ✨ NEW
- 🎨 **Modern Dark Theme:** Beautiful blue accents on dark background
- 🎯 **Enhanced Board Graphics:** Colorful tokens, shadows, and effects
- 📐 **Split Screen Layout:** Game board + chat side-by-side
- ✨ **Smooth Animations:** Hover effects and transitions
- 📱 **Responsive Design:** Resizable panels and adaptive layout

---

## ⚙️ Setup Instructions

### 1️⃣ Clone/Download the repository

```bash
# Clone or download the project
cd LudoMaster
```

### 2️⃣ Compile the project

**Option A - Using the batch file (Windows):**
```cmd
compile-enhanced.bat
```

**Option B - Manual compilation:**
```bash
javac -d bin -sourcepath src src\Main.java src\client\*.java src\model\*.java src\server\*.java src\util\*.java
```

### 3️⃣ Run the application

**Option A - Using Main.java (Recommended):**
```bash
cd bin
java Main
# Choose option 1 for Server or option 2 for Client
```

**Option B - Direct server/client launch:**

*Server:*
```bash
cd bin
java server.LudoServer
```

*Client (in separate terminal):*
```bash
cd bin
java client.LudoClient
```

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

