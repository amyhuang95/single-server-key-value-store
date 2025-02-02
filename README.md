# Single Server Key-Value Store (TCP & UDP)

## Overview

This project implements a **single-threaded key-value store server** that communicates with a **single client** using either **TCP or UDP**. The server supports three basic operations:

   ```
   PUT key value
   GET key
   DELETE key
   ```

The system allows users to choose their preferred communication protocol and ensures robust handling of **timeouts, malformed requests, and unsolicited responses**.

## How to Run

Compile the java classes and follow the below instruction:

### Start the Server:
```sh
java Server <port> <protocol>
```
- `<port>` – The port number the server should listen on
- `<protocol>` – Either `TCP` or `UDP`

### Start the Client:
```sh
java Client <host> <port> <protocol>
```
- `<host>` – The server's hostname or IP address
- `<port>` – The server’s port number
- `<protocol>` – Either `TCP` or `UDP`

## Project Structure

### Classes & Interfaces

- **CommunicationServer, CommunicationClient** – Defines the methods for the servers and clients of a specific protocol
- **TCPServer, UDPServer** – Implement `CommunicationServer` interface
- **TCPClient, UDPClient** – Implement `CommunicationClient` interface
- **KVStore** – Implements a **hash map** to manage key-value storage
- **Server, Client** – Orchestrates the server, client logic and allows protocol selection
- **Utils** – Contains shared utility methods (e.g., command-line validation)
