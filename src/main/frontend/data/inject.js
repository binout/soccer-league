const http = require('http')
const https = require('https')
const fs = require("fs");
const url = require('url');

// Get base URL from environment variable or command line argument, default to localhost:8080
const baseUrl = process.env.SOCCER_LEAGUE_URL || process.argv[2] || 'http://localhost:8080';
const parsedUrl = new URL(baseUrl);

console.log(`[INFO] Using base URL: ${baseUrl}`);

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
        hostname: parsedUrl.hostname,
        path: '/rest/players/' + encodeURIComponent(jsonPlayer.name),
        port: parsedUrl.port || (parsedUrl.protocol === 'https:' ? 443 : 80),
        method: 'PUT',
        headers: headers
    };

    const callback = function(response) {
        response.on('data', function(chunk) {
        });
        response.on('end', function() {
            console.log(jsonPlayer.name + " created");
        });
        response.on('error', function(error) {
            console.error('Error creating player ' + jsonPlayer.name + ':', error.message);
        });
    };

    const request = (parsedUrl.protocol === 'https:' ? https : http).request(options, callback);
    request.on('error', function(error) {
        console.error('Request error for player ' + jsonPlayer.name + ':', error.message);
    });
    request.write(JSON.stringify(jsonPlayer));
    request.end();
}