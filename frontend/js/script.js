const keycloak = new Keycloak({
  url: 'http://localhost:8081',
  realm: 'adso',
  clientId: 'adso-user'
});

keycloak.init({ onLoad: 'login-required' }).then(authenticated => {
  if (authenticated) {
    console.log('Authenticated');
    document.getElementById('logout').onclick = () => {
      keycloak.logout();
    };
  } else {
    console.log('Not authenticated');
  }
}).catch(error => {
  console.error('Failed to initialize', error);
});




const chatInput = document.querySelector("#chat-input");
const sendButton = document.querySelector("#send-btn");
const chatContainer = document.querySelector(".chat-container");
const themeButton = document.querySelector("#theme-btn");


let userText = null;
let ws = new WebSocket("ws://127.0.0.1:8080/chatbot");
let incomingChatDiv = null;
let pElement = null;



const createChatElement = (content, className) => {
  const chatDiv = document.createElement("div");
  chatDiv.classList.add("chat", className);
  chatDiv.innerHTML = content;
  return chatDiv;
}

ws.onmessage = function (event) {
  const typingAnimationDiv = document.querySelector('.typing-animation');
  if (typingAnimationDiv) {
    typingAnimationDiv.remove();
  }
  const textNode = document.createTextNode(event.data);
  pElement.appendChild(textNode);
  chatContainer.scrollTo(0, chatContainer.scrollHeight);

};


const copyResponse = (copyBtn) => {
  // Copy the text content of the response to the clipboard
  const reponseTextElement = copyBtn.parentElement.querySelector("p");
  navigator.clipboard.writeText(reponseTextElement.textContent);
  copyBtn.textContent = "done";
  setTimeout(() => copyBtn.textContent = "content_copy", 1000);
}



const handleOutgoingChat = () => {
  userText = chatInput.value.trim();
  if(!userText) return;
  chatInput.value = "";
  chatInput.style.height = `${initialInputHeight}px`;

  const html = `<div class="chat-content">
                            <div class="chat-details">
                                <p>${userText}</p>
                            </div>
                        </div>`;

  const outgoingChatDiv = createChatElement(html, "outgoing");
  chatContainer.querySelector(".default-text")?.remove();
  chatContainer.appendChild(outgoingChatDiv);
  chatContainer.scrollTo(0, chatContainer.scrollHeight);
  stopProcessing=false;

  ws.send(userText);
  const htmlResponse = `<div class="chat-content">
                                  <div class="chat-details">
                                      <div class="typing-animation">
                                          <div class="typing-dot" style="--delay: 0.2s"></div>
                                          <div class="typing-dot" style="--delay: 0.3s"></div>
                                          <div class="typing-dot" style="--delay: 0.4s"></div>
                                      </div>
                                  </div>
                                  <span onclick="copyResponse(this)" class="material-symbols-rounded">content_copy</span>
                                </div>`;
  incomingChatDiv = createChatElement(htmlResponse, "incoming");
  chatContainer.appendChild(incomingChatDiv);
  chatContainer.scrollTo(0, chatContainer.scrollHeight);
  pElement = document.createElement("p");
  pElement.textContent = event.data;
  incomingChatDiv.querySelector(".chat-details").appendChild(pElement);
  // 5000 milliseconds = 5 seconds
}


themeButton.addEventListener("click", () => {
  document.body.classList.toggle("light-mode");
  localStorage.setItem("themeColor", themeButton.innerText);
  themeButton.innerText = document.body.classList.contains("light-mode") ? "dark_mode" : "light_mode";
});

const initialInputHeight = chatInput.scrollHeight;

chatInput.addEventListener("input", () => {
  chatInput.style.height =  `${initialInputHeight}px`;
  chatInput.style.height = `${chatInput.scrollHeight}px`;
});

chatInput.addEventListener("keydown", (e) => {
  // If the Enter key is pressed without Shift and the window width is larger
  // than 800 pixels, handle the outgoing chat
  if (e.key === "Enter" && !e.shiftKey && window.innerWidth > 800) {
    e.preventDefault();
    handleOutgoingChat();
  }
});

sendButton.addEventListener("click", handleOutgoingChat);


document.addEventListener('DOMContentLoaded', () => {
  const sidebarItems = document.querySelectorAll('.sidebar-item');
  const panels = document.querySelectorAll('.sidebar-panel');

  sidebarItems.forEach(item => {
    item.addEventListener('click', () => {
      const panelId = item.getAttribute('data-panel');
      panels.forEach(panel => {
        if (panel.id === panelId) {
          panel.classList.toggle('show-panel');
        } else {
          panel.classList.remove('show-panel');
        }
      });
    });
  });
});



