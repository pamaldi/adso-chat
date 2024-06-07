package cloud.isaura.llama3.model;

import cloud.isaura.llama3.model.ollama.UserMessage;
import io.smallrye.mutiny.Multi;

import java.util.List;

public interface ChatLanguageModel
{


    Response<AiMessage> chat(List<UserMessage> messages);

    String generate(String prompt);

    Multi<String> generateStreaming(String prompt);
}
