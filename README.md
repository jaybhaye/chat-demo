# Intro
Demo Chat Server and Client.



## Build

`cd ${chat-demo-root}`
`mvn protobuf:compile`
`mvn clean install`

This will create uber jars in both client and server target directories (chat-client/target and chat-server/target). 




## Run

Uber jars can be run as normal Java Jars. e.g. to run server, run

`java -jar chat-server/target/chat-server-1.0-SNAPSHOT-jar-with-dependencies.jar`

The client has a REPL, as soon as you run client jar, REPL loop will start running.

Auth Loop should be intuitive.

In Message loop, to send a message to user, enter it in {Receiver User Id}:{Message} format. 

i.e. To send message "Hello There" to userId "foobar", type

`foobar:Hello There` 



## Redis Keys for Passwords

Rest of the Application does not need any configuration, but you'll need to seed Redis with Password for each user.
Passwords are currently stored in Redis in Plaintext. Password for userId "abc" is stored at Key "abc_password".

To add a user "abc", run following in redis-cli 

`set abc_password password`



## Assumptions

Everything runs locally - Chat Server and Redis. 
