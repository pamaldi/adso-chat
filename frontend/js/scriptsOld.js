let reset = true;
let messageId = '';
let messageIdCounter = 0;
const messageInput = $('.message_input');
let stopProcessing = false;

document.addEventListener('DOMContentLoaded', function () {
  var message_side = 'left';
  const chatbox = document.getElementById('chatbox');
  const interruptBtn = document.querySelector('.interrupt_button');
  let ws = new WebSocket("ws://127.0.0.1:8080/chatbot");


  ws.onmessage = function (event) {
    if (stopProcessing) {
        reset = true;
        return
    }
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

  interruptBtn.addEventListener('click', () => {
  stopProcessing=true;
  });


  messageInput.on('input', function () {
    adjustTextareaHeight(this);
  });

  function sendMessage(text) {
    $('.message_input').val('');
    displayMessage('Adso', text);
    ws.send(text);
    messageBuffer = '';
    messageInput.val('');
    adjustTextareaHeight(messageInput[0]);
  };

  function verifyMessage() {
    stopProcessing=false;
    var input = $('.message_input').val();
    if (input != '') {
      sendMessage(input);
    }
  };


  $('.send_message').click(function () {
    verifyMessage()
  });

  $('.message_input').on('keydown', function (event) {
    if (event.key === 'Enter') {
      verifyMessage()
    }
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

  function adjustTextareaHeight(textarea) {
    textarea.style.height = 'auto'; // Reset the height
    const lineHeight = 20; // Set the line height in pixels
    const maxHeight = lineHeight * 6; // Calculate max height based on 6 lines

    if (textarea.scrollHeight > maxHeight) {
      textarea.style.height = maxHeight + 'px'; // Set height to maxHeight
      textarea.style.overflowY = 'scroll'; // Enable scrolling
    } else {
      textarea.style.height = textarea.scrollHeight + 'px'; // Expand to fit content
      textarea.style.overflowY = 'hidden'; // Hide scrollbar
    }
  }


});




