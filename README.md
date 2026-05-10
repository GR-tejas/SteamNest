# StreamNest

StreamNest is a local network media streaming system for streaming videos and images across devices connected to the same network.

The project currently includes:

- StreamNestServer (ASP.NET Core backend server)
- StreamNestServerUI (Windows server launcher UI)
- StreamNestClient (Android client app)

---

# Features

- Stream videos and images over local Wi-Fi
- Configurable ports
- Persistent server configuration
- Folder-based media sharing
- Lightweight local streaming architecture

---

# Server Setup

There are 2 ways to start the server.

## Method 1 — Using StreamNestServerUI.exe (Recommended)

IMPORTANT:
Keep BOTH:

- `StreamNestServerUI.exe`
- `StreamNestServer.exe`

in the SAME folder.

Then:

1. Launch `StreamNestServerUI.exe`
2. Enter a port number
3. Select the folder you want to share
4. Press the **Start Server** button

The application will display:
- IP Address
- Port Number
- Server Status

---

## Method 2 — Manual CMD Launch

You can also start the server manually using only:

`StreamNestServer.exe`

Open CMD in the same folder and run:

```cmd
.\StreamNestServer.exe [Port] "[Folder Path]"
```

Example:

```cmd
.\StreamNestServer.exe 1234 "D:\SharedFolder"
```

---

# Port Notes

- Maximum allowed port: `65535`
- Recommended minimum port: `2000`

Lower-numbered ports are commonly reserved by Windows, system services, or other applications.

---

# Android Client Setup

1. Install the APK
2. Connect the Android device to the SAME network as the server
3. Open the app
4. Enter:
   - Server IP Address
   - Port Number
5. Press **Submit**

---

# Finding the IP Address

## If using StreamNestServerUI

The IP address is shown automatically in the UI.

## If using CMD method

Run:

```cmd
ipconfig
```

Then use the IPv4 Address shown in CMD.

---

# Technologies Used

- Kotlin
- Jetpack Compose
- ASP.NET Core
- WinForms
- HTTP Networking
- JSON APIs

---

# Future Plans

- Windows client app
- Search functionality
- Thumbnail generation
- Improved media player
- Other QOL features
