fetch('https://fr.wikipedia.org/wiki/Informatique')
  .then(response => response.text())
  .then(html => {
    const words = extractWords(html);
    writeToFile(words);
  })
  .catch(error => console.error('Erreur lors de la récupération de la page Wikipedia :', error));

  function extractWords(html) {
    const cheerio = require('cheerio');
    const char = cheerio.load(html);
    const text = char('body').text();
    const regex = /[a-zA-ZÀ-ÖØ-öø-ÿ]+/g; 
    const matches = text.match(regex); 
    const wordsSet = new Set(matches); 
    const words = Array.from(wordsSet);
    return words;
  }
  
function writeToFile(words) {
  const fs = require('fs');
  const filename = 'dictionary.txt';
  const content = words.join('\n'); 
  fs.writeFileSync(filename, content, { flag: 'a' });
  console.log('Fichier de dictionnaire créé avec succès');
}

