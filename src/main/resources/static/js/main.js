'use strict';

let usernamePage = document.querySelector('#username-page');
let chatPage = document.querySelector('#chat-page');
let usernameForm = document.querySelector('#usernameForm');
let messageForm = document.querySelector('#messageForm');
let messageInput = document.querySelector('#message');
let messageArea = document.querySelector('#messageArea');
let connectingElement = document.querySelector('.connecting');
let userListArea = document.querySelector('#userListArea');

let stompClient = null;
let userName = null;
let userEmail = null;

let colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
    userName = document.querySelector('#userName').value.trim();
    userEmail = document.querySelector('#userEmail').value.trim();

    if (userName) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        let socket = new SockJS('/messages-sock');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}

function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public', onMessageReceived);

    // Tell your username to the server
    stompClient.send("/app/chat.addUser", {}, JSON.stringify({sender: userName, email: userEmail, status: 'JOIN'}));

    connectingElement.classList.add('hidden');
    stompClient.subscribe('/topic/users', getUserList);
    stompClient.send("/app/chat.getUsers", {}, {});
}

function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

function sendMessage(event) {
    let messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        let chatMessage = {
            sender: userName,
            email: userEmail,
            content: messageInput.value,
            status: 'CHAT'
        };

        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

function onMessageReceived(payload) {
    let message = JSON.parse(payload.body);

    let messageElement = document.createElement('li');

    if (message.status === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' присоединился(ась) к чату!';

    } else if (message.status === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' покинул(а) чат!';

    } else {
        messageElement.classList.add('chat-message');

        let avatarElement = document.createElement('i');
        let avatarText = document.createTextNode(message.sender[0]); // первая буква имени на аватарке
        console.log("sender = " + message.sender +
            " email = " + message.email +
            " status = " + message.status +
            " content = " + message.content);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        let usernameElement = document.createElement('span');
        let usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    let textElement = document.createElement('p');
    let messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function getAvatarColor(messageSender) {
    let hash = 0;
    for (let i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    let index = Math.abs(hash % colors.length);
    return colors[index];
}

function getUserList(payload) { // TODO неверно работает список, переделать после фикса на ОНЛАЙН
    let listUsers = JSON.parse(payload.body); // получаем список всех пользователей
    for (let i = 0; i <= listUsers.length; i++) {
        let tableRowElement = document.createElement('tr');

        let userNameElement = document.createElement('td');
        let userNameText = document.createTextNode(listUsers[i].userName); // имя

        let userEmailElement = document.createElement('td');
        let userEmailText = document.createTextNode(listUsers[i].email); // email

        userNameElement.appendChild(userNameText);
        userEmailElement.appendChild(userEmailText);
        tableRowElement.appendChild(userNameElement);
        tableRowElement.appendChild(userEmailElement);

        userListArea.appendChild(tableRowElement);
    }
    // userListArea.scrollTop = userListArea.scrollHeight;
}

usernameForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', sendMessage, true);