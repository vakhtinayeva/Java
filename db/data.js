const { faker } = require('@faker-js/faker');
const fs = require('fs');

const data = [];
for(let i = 0; i < 10000; i++) {
    let firstName = faker.name.firstName();
    let lastName = faker.name.lastName();
    let email = faker.internet.email();
    let age = new Date().getFullYear() - new Date(faker.date.birthdate()).getFullYear();
    let country = faker.address.country()
    let city = faker.address.cityName();
    let street = faker.address.streetAddress(true);
    let genre = faker.music.genre();
    let song = faker.music.songName();

    data.push([firstName, lastName, email, age, country, city, street, genre, song]);
}


let file = fs.createWriteStream('data.txt');
file.on('error', (err) => { console.log(err)});
data.forEach((x) => { file.write(x.join(', ') + '\n'); });
file.end();
