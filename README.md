# LudoMaster
A multiplayer Ludo Game built using **Java Network Programming concepts**, featuring a **fully integrated chat system**!

**Key Technologies:**
- TCP Sockets  
- Object Streams  
- Multithreading  
- Client-Server Communication  
- Modern Swing GUI with Dark Theme
- Real-time Chat (Group & Private Messaging)

## Features

### Game Features
- **Multiplayer Support:** 1–4 players (local or across LAN)
- **Multithreaded Server:** Handles each player on a separate thread  
- **Real-time Updates:** Instant game state synchronization
- **Bot Players:** Automatically added when fewer than 4 players  
- **Turn-based Gameplay:** Classic Ludo rules with dice rolling
- Demonstrates: `Socket`, `ServerSocket`, `Thread`, `ObjectInputStream`, `ObjectOutputStream`

### Chat Features 
- **Group Chat:** Broadcast messages to all players
- **Private Messaging:** One-on-one conversations with any player
- **User List:** See all connected players
- **Modern UI:** WhatsApp-style message bubbles
- **Timestamps:** See when each message was sent
- **Tabbed Interface:** Separate tabs for each conversation

## Setup Instructions

### Clone/Download the repository

```bash
# Clone or download the project
cd LudoMaster
```

### Compile the project

**Using the batch file (Windows):**
```cmd
compile-enhanced.bat
```

### Run the application

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

### Gameplay Modes
- 1 Player	You play against 3 server bots	1 client connected  
- 2 Players	Two human players, two bots	2 clients connected  
- 3 Players	Three humans, one bot	3 clients connected  
- 4 Players	Full match — four human players	4 clients connected  

Game Ends: When all players finish (basic logic).

### User Interface
<img width="1484" height="929" alt="image" src="https://github.com/user-attachments/assets/2a0663e7-e447-40cb-b894-6027931d3dce" />

### Contributors (for assignment)
- Dimuthu Dilshan  
- Thilshath SMT  
- Lasini Pallewaththa  
- Piyumi Jayathilaka  
- Shavmiyan  
