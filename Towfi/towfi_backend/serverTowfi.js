var redis=require('redis');
var port=8081                  
var client=redis.createClient(port);
client.on('connect', function() {
    console.log('connected');
});

                  