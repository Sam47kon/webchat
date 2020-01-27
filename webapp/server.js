const mongoClient = require('mongodb').MongoClient;
const clientSocket = require('socket.io').listen(4000).sockets;

let url = 'mongodb://localhost:27017';
let dataBaseName = 'mongochat';
let tableName = 'chats';


mongoClient.connect(url, {
    useUnifiedTopology: true,
    useNewUrlParser: true,
})
    .then((client) => {
        console.log('MongoDB Connected...');

        let db = client.db(dataBaseName);
        // Проверка
        db.collection(tableName).find().toArray((err, results) => {
            if (err) throw  err;
            results.forEach(value => console.log("Name: " + value.name + ", message: " + value.message));
        });


        // Connect to Socket.io
        clientSocket.on('connection', function (socket) {
            let chat = db.collection('chats');

            // Функция отправки статуса
            sendStatus = function (s) {
                socket.emit('status', s);
            };

            // Получение чата из коллекции mongoClient
            chat.find().limit(100).sort({_id: 1}).toArray(function (err, res) {
                if (err) {
                    throw err;
                }

                // Emit message
                socket.emit('output', res);
            });


            // Handle input events
            socket.on('input', function (data) {
                let name = data.name;
                let message = data.message;

                // Check for name and message
                if (name == '' || message == '') {
                    // send error status
                    sendStatus('Введите имя и сообщение')
                } else {
                    // Insert message
                    chat.insert({name: name, message: message}, function () {
                        clientSocket.emit('output', [data]);

                        // Send status object
                        sendStatus({
                            message: 'Messaage sent',
                            clear: true
                        });
                    });
                }
            });
            // Handle clear
            socket.on('clear', function (data) {
                // Remove all message from collection
                chat.remove({}, function () {
                    // Emit cleared
                    socket.emit('cleared');
                });
            });
        });


    })
    .catch(err => console.log(err));


// Подключение к mongoClient
// mongoClient.connect(url, function (err, clientDb) {
//     if (err) {
//         throw err;
//     }
//     console.log('MongoDB Connected...');
//
//     // Connect to Socket.io
//     clientSocket.on('connection', function (socket) {
//         let db = clientDb.db(dataBaseName);
//         let chat = db.collection('chats');
//
//         // Функция отправки статуса
//         sendStatus = function (s) {
//             socket.emit('status', s);
//         };
//
//         // Получение чата из коллекции mongoClient
//         chat.find().limit(100).sort({_id: 1}).toArray(function (err, res) {
//             if (err) {
//                 throw err;
//             }
//
//             // Emit message
//             socket.emit('output', res);
//         });
//
//
//         // Handle input events
//         socket.on('input', function (data) {
//             let name = data.name;
//             let message = data.message;
//
//             // Check for name and message
//             if (name == '' || message == '') {
//                 // send error status
//                 sendStatus('Введите имя и сообщение')
//             } else {
//                 // Insert message
//                 chat.insert({name: name, message: message}, function () {
//                     clientSocket.emit('output', [data]);
//
//                     // Send status object
//                     sendStatus({
//                         message: 'Messaage sent',
//                         clear: true
//                     });
//                 });
//             }
//         });
//         // Handle clear
//         socket.on('clear', function (data) {
//             // Remove all message from collection
//             chat.remove({}, function () {
//                 // Emit cleared
//                 socket.emit('cleared');
//             });
//         });
//     });
// });