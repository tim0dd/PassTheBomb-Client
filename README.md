# PassTheBomb-Client

To be fully function the app relies on a REST server. The code for the server can be found here: https://github.com/tditt/PassTheBomb-Server

## Testing
- **In emulator**:  For testing the server capabilities with android emulator, the preset IP will work if the server is running on the same system as the emulator.
  
- **On a device**:  For testing the server capabilities with a device, the server IP has to be set manually in the code. The server has to be run in the same network. In the RestService class there is a constant with the name REST_URL, which has to be set to the server IP in the network. It runs on port 8080 on default, so the IP should include this port number.
