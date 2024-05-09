package cloud.isaura.llama3.model;

import cloud.isaura.llama3.model.ollama.UserMessage;


import java.util.List;

public interface ChatLanguageModel
{


    Response<AiMessage> chat(List<UserMessage> messages);

    String generate(String prompt);
}
