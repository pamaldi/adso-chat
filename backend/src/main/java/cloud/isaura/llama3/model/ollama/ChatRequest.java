package cloud.isaura.llama3.model.ollama;

import java.util.List;

public record ChatRequest(String model,
                          List<Message> messages,
                          Options options,
                          String format,
                          Boolean stream) {
}
