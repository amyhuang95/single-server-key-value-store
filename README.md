## Assignment Overview

- the purpose & the scope (1 paragraph, ~250 words)
- technical impression while working on this project (1-2 paragraphs, 200-500 words)
  - 1 use case where I would apply this in practice

# Key-Value Store (Single-Threaded)

- two network protocols: TCP, UDP
  - 2 clients + 2 servers
- Methods: 
  - PUT(key, value)
  - GET(key)
  - DELETE(key)
- Data Structure: Hash Map

## The Client Program

- ✅ takes 2 command line arguments: 
  - `hostname` or `ip_address`, and
  - `port_number`
- uses timeout mechanism to deal with server failure (unresponsive)
  - if it does not receive a response to a request, note it in client log, and send the remaining requests
- protocol to communicate packet contents for the 3 requests types (PUT, GET, DELETE)
- robust to datagram packets that were not requested or not properly formed
  - reported it in a human-readable way, e.g., "received unsolicited response", "acknowledging unknown PUT/GET/DELETE with an invalid key"
- client log is timestamped with current system time (millisecond precision)
- pre-populates the key-val store with data and a set of keys
  - and do at least 5 of each operation

## The Server Program

- takes 1 command line argument:
  - `port_number` to listen for datagram packets on
- run forever until killed manually
- displays the requests received and its responses, in human-readable way
  - print to the server log "received query from `InetAddress` `port number` for a specific word"
- robust to datagram packets that were not requested or not properly formed
  - print in server log "received malformed request of length `n` from `address`:`port`" 
- server log is timestamped with current system time (millisecond precision)
