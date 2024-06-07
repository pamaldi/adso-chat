package cloud.isaura.llama3.chat;
import cloud.isaura.llama3.model.ollama.CompletionResponse;
import cloud.isaura.llama3.model.ollama.OllamaLanguageModel;
import cloud.isaura.llama3.model.ollama.OllamaModel;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@ServerEndpoint("/chatbot")
public class WebSocketEndpoint
{

    private static final Logger LOGGER = Logger.getLogger("WebSocket");
    @Inject
    ManagedExecutor managedExecutor;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

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
        LOGGER.info("WebSocket message received: " + message);
        Multi<String> dataStream = this.ollamaLanguageModel.generateStreaming(message);
        dataStream.subscribe().with(
                item -> sendMessage(session, item),
                failure -> handleFailure(session, failure),
                () -> handleCompletion(session)
        );
    }

    private void sendMessage(Session session, String message) {
        executorService.submit(() -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                LOGGER.error("Error sending message", e);
            }
        });
    }

    private void handleFailure(Session session, Throwable throwable) {
        LOGGER.error("Data stream failed", throwable);
        try {
            session.close();
        } catch (IOException e) {
            LOGGER.error("Error closing session", e);
        }
    }

    private void handleCompletion(Session session) {
        LOGGER.info("Data stream completed");
        /*
        try {
            session.close();
        } catch (IOException e) {
            LOGGER.error("Error closing session", e);
        }*/
    }

    static String baseUrl() {
        return "http://ollama-llama3:11434";
    }
}
