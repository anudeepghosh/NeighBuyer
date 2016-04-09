var redis = require('redis')
var http = require('http');
var fs = require('fs');
var url = require('url');
var path = require('path');
var express = require('express');
var app = express();
var io = require('socket.io')(http);

// Create a server
app.use(express.static(path.join(__dirname, 'public')));
/**
    Listening to clients at port 8081
**/
var server = app.listen(8081, function () {

  var host = server.address().address
  var port = server.address().port

})

app.get('/index', function (req, res) {
    console.log("Client connected IP: " + req.connection.remoteAddress);
    res.sendFile( __dirname + "/" + "index.html" );
});

app.get('/index.html', function (req, res) {
    console.log("Client connected IP: " + req.connection.remoteAddress);
    res.sendFile( __dirname + "/" + "index.html" );
});
console.log("Server running ");