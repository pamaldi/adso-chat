package cloud.isaura.llama3.chat;
import cloud.isaura.llama3.model.ollama.OllamaLanguageModel;
import cloud.isaura.llama3.model.ollama.OllamaModel;
import jakarta.inject.Inject;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.jboss.logging.Logger;

import java.time.Duration;


@ServerEndpoint("/chatbot")
public class WebSocketEndpoint
{

    private static final Logger LOGGER = Logger.getLogger("WebSocket");
    @Inject
    ManagedExecutor managedExecutor;

    private OllamaLanguageModel ollamaLanguageModel;

    public WebSocketEndpoint() {
        LOGGER.debug("start ws");
        this.ollamaLanguageModel = new OllamaLanguageModel(OllamaModel.LLAMA3,null,"json",baseUrl(), Duration.ofSeconds(60));
    }

    @OnOpen
    public void onOpen(Session session) {
        LOGGER.info("open");

    }

    @OnClose
    void onClose(Session session) {
        LOGGER.info("close");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        LOGGER.info("Message from chat "+message);
        managedExecutor.execute(() -> {
            try {
                LOGGER.debug("chat request "+message);
                String generate = this.ollamaLanguageModel.generate(message);
                LOGGER.debug("ai message "+generate);
                session.getBasicRemote().sendText(generate);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }

    static String baseUrl() {
        return "http://ollama-llama3:11434";
    }
}
