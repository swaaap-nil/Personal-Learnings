//used for building notification services. or live chat rooms
//uses websocket: take it as a wrap around over http. http creates new connectin for each rqst and connection is deleted once the task has been finish automatically
//websocket is a full duplex connection 

const http = require('http');
const readline = require("readline-sync"); 
const webSocketServer = require("websocket").server;
const fs = require('fs').promises;

//array to maintain list of connected users
let connections = [];

const requestListener = function (req, res) {
    fs.readFile(__dirname + "/public/landing.html")
        .then(contents => {
            res.setHeader("Content-Type", "text/html");
            res.writeHead(200);
            res.end(contents);
        })
        .catch(err => {
            res.writeHead(500);
            res.end(err);
            return;
        });
};

//a normal raw http server
const rawHTTPServer = http.createServer(requestListener);

//pass this raw server to websocket library which converts it to a websocket

const websocket =  new webSocketServer({"httpServer": rawHTTPServer})

//Listen on TCP Socket

rawHTTPServer.listen(8080,()=> console.log("Server Listening on port 8080"));

//when a request comes to that http server with a ws protocol it is redirected to websocket

websocket.on("request", (request)=>{
    
    console.log(request.socket.remotePort," wants to connect.")
    let perm = null;

    while (perm !== "Y" && perm !== "N") {
        console.log("Enter Y/N");
        perm = String(readline.question());
    }
    if(perm === "N"){
        request.reject( { reason: "admin denied"});

    }
        const connection = request.accept(null ,request.origin);
        
        connections.push(connection)
        connections.forEach(c=>c.send(`User${connection.socket.remotePort} just connected.`))

        
        connection.on("message",message =>{
            connections.forEach(c=>{c.send(`User${connection.socket.remotePort} says: ${message.utf8Data}`)})
        })

        connection.on("close",()=>{
            console.log(`User ${connection.socket.remotePort} has left`);
            connections.forEach(c=>{c.send(`User${connection.socket.remotePort} left the room`)})
        })
    
})


//how client connects
// let ws = new WebSocket("ws://localhost:8080");
// ws.onmessage = message => console.log(message.data);
// ws.send("hi")

