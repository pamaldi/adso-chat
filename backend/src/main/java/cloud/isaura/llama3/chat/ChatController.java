package cloud.isaura.llama3.chat;

import cloud.isaura.llama3.model.AiMessage;
import cloud.isaura.llama3.model.ChatLanguageModel;
import cloud.isaura.llama3.model.Response;
import cloud.isaura.llama3.model.ollama.OllamaLanguageModel;
import cloud.isaura.llama3.model.ollama.OllamaModel;
import cloud.isaura.llama3.model.ollama.UserMessage;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Path("/chat")
public class ChatController
{
    private static final Logger LOGGER = Logger.getLogger("ChatController");

    private OllamaLanguageModel ollamaLanguageModel;

    public ChatController() {
        this.ollamaLanguageModel = new OllamaLanguageModel(OllamaModel.LLAMA3,null,"json",baseUrl(), Duration.ofSeconds(60));
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String chat(ChatRequest chatRequest)
    {
        LOGGER.debug("chat request "+chatRequest);
        String generate = this.ollamaLanguageModel.generate(chatRequest.getMessage());
        LOGGER.debug("ai message "+generate);
        return generate;
    }

    static String baseUrl() {
        return "http://ollama-llama3:11434";
    }
}
