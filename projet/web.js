const http = require('http');

var server = http.createServer();

server.on('request', function(req, res) {
  res.statusCode = 200;
  res.setHeader('Content-Type', 'text/plain');
  res.end('First page !');
});

server.on('listen', function() {
  console.log('Server started!');
})

server.listen(8080);