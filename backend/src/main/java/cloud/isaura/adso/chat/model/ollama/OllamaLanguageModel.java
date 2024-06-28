package cloud.isaura.adso.chat.model.ollama;

import cloud.isaura.adso.chat.model.AiMessage;
import cloud.isaura.adso.chat.model.ChatLanguageModel;
import cloud.isaura.adso.chat.model.Response;
import cloud.isaura.adso.chat.model.TokenUsage;
import cloud.isaura.adso.chat.utils.ValidationUtils;
import io.smallrye.mutiny.Multi;
import org.jboss.logging.Logger;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static cloud.isaura.adso.chat.utils.ValidationUtils.ensureNotEmpty;


public class OllamaLanguageModel implements ChatLanguageModel {

    private static final Logger LOGGER = Logger.getLogger("OllamaLanguageModel");

    private final OllamaModel model;
    private final Options options;
    private final String format;
    private final OllamaClient client;

    public OllamaLanguageModel(OllamaModel ollamaModel, Options options, String format, String baseUrl, Duration timeout) {
        this.model = (OllamaModel) ValidationUtils.ensureNotNull(ollamaModel, "modelName");
        this.options = options;
        this.format = format;
        this.client = new OllamaClient(baseUrl,timeout);
    }




    @Override
    public Response<AiMessage> chat(List<UserMessage> userMessages) {
        ensureNotEmpty(userMessages, "messages");
        LOGGER.debug("messages "+userMessages);
        List<Message> messages = new ArrayList<>();
        userMessages.stream().forEach(userMessage -> messages.add(new Message(userMessage.getText(), Role.USER)));
        ChatRequest request = new ChatRequest(
                model.getName(),
                messages,
                options,
                format,
                false
                );

        ChatResponse response = client.chat(request);
        LOGGER.debug("chat response "+response.message().content());
        return Response.from(
                AiMessage.from(response.message().content()),
                new TokenUsage(response.promptEvalCount(), response.evalCount())
        );
    }

    @Override
    public String generate(String prompt) {
        LOGGER.debug("prompt "+prompt);
        CompletionRequest completionRequest = new CompletionRequest(model.getName(),"",prompt,null,null,false);
        CompletionResponse completionResponse = client.generate(completionRequest);
        LOGGER.debug("chat response "+completionResponse.response());
        return completionResponse.response();
    }

    @Override
    public Multi<String> generateStreaming(String prompt) {
        LOGGER.debug("prompt "+prompt);
        CompletionRequest completionRequest = new CompletionRequest(model.getName(),"",prompt,null,null,true);
        return client.getDataStreamString(completionRequest);
    }

}
