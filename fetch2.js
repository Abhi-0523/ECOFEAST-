const https = require('https');
https.get('https://www.istockphoto.com/photo/raw-chicken-drum-stick-or-leg-pieces-gm1351966480-427525690', (res) => {
  let data = '';
  res.on('data', chunk => data += chunk);
  res.on('end', () => {
    const match = data.match(/<meta property="og:image" content="([^"]+)"/);
    if (match) console.log(match[1]);
    else console.log('Not found');
  });
}).on('error', err => console.log(err.message));
