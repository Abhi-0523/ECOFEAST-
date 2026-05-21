const https = require('https');
const urls = [
  'https://unsplash.com/photos/ripe-pineapple-fruit-nAOZCYcLND8',
  'https://unsplash.com/photos/red-apple-on-white-surface-XiWQbLEhFyo',
  'https://unsplash.com/photos/yellow-bananas-fczCr7MdE7U',
  'https://unsplash.com/photos/yellow-and-red-round-fruit-haSJEJYzl5A'
];

urls.forEach(url => {
  https.get(url, (res) => {
    let data = '';
    res.on('data', chunk => data += chunk);
    res.on('end', () => {
      const match = data.match(/<meta property="og:image" content="([^"]+)"/);
      if (match) {
        console.log(match[1]);
      } else {
        console.log('No og:image found for ' + url);
      }
    });
  }).on('error', err => console.log(err.message));
});
