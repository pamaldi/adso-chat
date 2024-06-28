package cloud.isaura.adso.chat.model;

import cloud.isaura.adso.chat.model.ollama.UserMessage;
import io.smallrye.mutiny.Multi;

import java.util.List;

public interface ChatLanguageModel
{


    Response<AiMessage> chat(List<UserMessage> messages);

    String generate(String prompt);

    Multi<String> generateStreaming(String prompt);
}
