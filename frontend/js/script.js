const chatInput = document.querySelector("#chat-input");
const sendButton = document.querySelector("#send-btn");
const chatContainer = document.querySelector(".chat-container");
const themeButton = document.querySelector("#theme-btn");
const deleteButton = document.querySelector("#delete-btn");

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
  ws.send(userText);
  const htmlResponse = `<div class="chat-content">
                    <div class="chat-details">
                    </div>
                    <span onclick="copyResponse(this)" class="material-symbols-rounded">content_copy</span>
                </div>`;
  incomingChatDiv = createChatElement(htmlResponse, "incoming");
  chatContainer.appendChild(incomingChatDiv);
  chatContainer.scrollTo(0, chatContainer.scrollHeight);
  pElement = document.createElement("p");
  pElement.textContent = event.data;
  incomingChatDiv.querySelector(".chat-details").appendChild(pElement);
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

