
document.addEventListener('DOMContentLoaded', function () {
  var message_side = 'left';
  const ws = new WebSocket("ws://127.0.0.1:8080/chatbot");
  const chatbox = document.getElementById('chatbox');

  ws.onmessage = function (event) {
    const message = event.data;
    displayMessage('Strabone', message);
  };

  function sendMessage(text) {
    $('.message_input').val('');
    displayMessage('Adso', text);
    ws.send(text);

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
    messageElement.innerHTML = `<b>${sender}:</b> ${message}`;
    chatbox.appendChild(messageElement);
    chatbox.scrollTop = chatbox.scrollHeight; // Scroll to the bottom
  }

});
