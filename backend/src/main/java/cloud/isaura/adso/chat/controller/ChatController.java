package cloud.isaura.adso.chat.controller;

import cloud.isaura.adso.chat.configuration.Configuration;
import cloud.isaura.adso.chat.model.ollama.OllamaLanguageModel;
import cloud.isaura.adso.chat.model.ollama.OllamaModel;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestSseElementType;

import java.time.Duration;

@Path("/chat")
public class ChatController
{
    private static final Logger LOGGER = Logger.getLogger("ChatController");

    @Inject
    private Configuration configuration;

    private OllamaLanguageModel ollamaLanguageModel;

    public ChatController() {
        this.ollamaLanguageModel = new OllamaLanguageModel(OllamaModel.LLAMA3,null,"json", configuration.getOllamaEndpoint(), Duration.ofSeconds(60));
    }

    @Path("raw")
    @POST
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestSseElementType(MediaType.APPLICATION_JSON)
    public Multi<String> chat(ChatRequest chatRequest)
    {
        LOGGER.debug("chat request "+chatRequest);
        return this.ollamaLanguageModel.generateStreaming(chatRequest.getMessage());

    }

}
