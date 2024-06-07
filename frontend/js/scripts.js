let reset = true;
let messageId = '';
let messageIdCounter = 0;

document.addEventListener('DOMContentLoaded', function () {
  var message_side = 'left';
  const ws = new WebSocket("ws://127.0.0.1:8080/chatbot");
  const chatbox = document.getElementById('chatbox');

  ws.onmessage = function (event) {
    if (reset) {
      messageId = displayMessage('Guglielmo', event.data);
      reset = false;
    } else {
      updateMessage(messageId, event.data.replace(/####$/, ''));
      const delimiterIndex = event.data.indexOf('####');
      if (delimiterIndex !== -1) {
        reset = true;
        messageId = ''
      }
    }


  };

  function sendMessage(text) {
    $('.message_input').val('');
    displayMessage('Adso', text);
    ws.send(text);
    messageBuffer = '';
  };

  function verifyMessage() {
    var input = $('.message_input').val();
    if (input != '') {
      sendMessage(input);
    }
  };


  $('.send_message').click(function () {
    verifyMessage()
  });

  function displayMessage(sender, message) {
    const messageElement = document.createElement('div');
    const messageId = `message-${messageIdCounter++}`;
    messageElement.setAttribute('id', messageId);
    messageElement.innerHTML = `<b>${sender}:</b> <span class="message-content">${message}</span>`;
    chatbox.appendChild(messageElement);
    chatbox.scrollTop = chatbox.scrollHeight; // Scroll to the bottom

    return messageId;
  }

  function updateMessage(messageId, newMessage) {
    const messageElement = document.getElementById(messageId);
    if (messageElement) {
       const messageContent = messageElement.querySelector('.message-content');
       if (messageContent) {
           messageContent.innerHTML += ` ${newMessage}`;
      }
    }
  }


});
