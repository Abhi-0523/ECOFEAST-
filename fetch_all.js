const https = require('https');
const urls = [
  'https://unsplash.com/photos/ripe-pineapple-fruit-nAOZCYcLND8',
  'https://unsplash.com/photos/red-apple-on-white-surface-XiWQbLEhFyo',
  'https://unsplash.com/photos/yellow-bananas-fczCr7MdE7U',
  'https://unsplash.com/photos/a-pile-of-tomatoes-M7Mb3hRvoh0',
  'https://unsplash.com/photos/orange-carrots-on-human-hand-ZgDHMMd72I8',
  'https://unsplash.com/photos/a-pile-of-red-radishes-sitting-on-top-of-green-leaves-OPZzH1WJ6jQ',
  'https://unsplash.com/photos/native-egg-lot-leOh1CzRZVQ',
  'https://unsplash.com/photos/brown-bread-on-brown-wooden-tray-w2ZFjDnUL3w',
  'https://unsplash.com/photos/a-pile-of-bagels-sitting-on-top-of-a-cooling-rack-WMX_0xodxnw',
  'https://unsplash.com/photos/a-piece-of-cheese-on-a-wooden-plate-with-a-knife-IdZchyqCNJQ',
  'https://unsplash.com/photos/strawberry-and-blackberry-on-clear-glass-display-counter-1WmlAiYgnoI',
  'https://unsplash.com/photos/a-pile-of-doughnuts-sitting-on-top-of-a-white-plate-bm1dJYcGJLw'
];

async function fetchAll() {
  for (let i = 0; i < urls.length; i++) {
    const url = urls[i];
    await new Promise(resolve => {
      https.get(url, (res) => {
        let data = '';
        res.on('data', chunk => data += chunk);
        res.on('end', () => {
          const match = data.match(/<meta property="og:image" content="([^"]+)"/);
          if (match) {
            console.log((i+1) + " : " + match[1].replace(/\?.*/, '') + '?w=500&h=350&fit=crop');
          } else {
            console.log((i+1) + " : FAILED");
          }
          resolve();
        });
      }).on('error', err => {
        console.log(err.message);
        resolve();
      });
    });
  }
}
fetchAll();
