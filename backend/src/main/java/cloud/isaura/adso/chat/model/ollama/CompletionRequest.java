package cloud.isaura.adso.chat.model.ollama;

public record CompletionRequest(String model,
                                String system,
                                String prompt,
                                Options options,
                                String format,
                                Boolean stream) {
}
