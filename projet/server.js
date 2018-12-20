const express = require('express');

const app = express();

app.get('/', function(req, res) {
    res.send('Hello !');
});

app.get('/home', function(req, res) {
  res.send('Maison :-)');
});

app.get('/user/:uid', function(req, res) {
  res.send('Bonjour utilisateur ' + req.params.uid);
});

app.use(function(req, res, next){
  res.status(404).send('Page introuvable !');
});

app.listen(3000, function() {
  console.log('Example app listening on port 3000!');
});
