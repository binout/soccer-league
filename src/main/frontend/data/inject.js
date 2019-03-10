const http = require('http')
const fs = require("fs");

console.log("[BEGIN] Read players.json");
const content = fs.readFileSync("data/players.json");
const jsonContent = JSON.parse(content);
console.log("[END] Read players.json -> " + jsonContent.length + " players");

console.log("[BEGIN] Inject data");
jsonContent.forEach(json => createPlayer(json))
console.log("[END] Inject data");

function createPlayer(jsonPlayer) {
    const headers = {
        'Content-Type': 'application/json',
    };
    const options = {
        host: 'localhost',
        path: '/rest/players/' + encodeURIComponent(jsonPlayer.name),
        port: 8080,
        method: 'PUT',
        headers: headers
    };
    const callback = function(response) {
        response.on('data', function(chunk) {
        });
        response.on('end', function() {
            console.log(jsonPlayer.name + " created");
        });
    };
    http.request(options, callback).write(JSON.stringify(jsonPlayer));
}