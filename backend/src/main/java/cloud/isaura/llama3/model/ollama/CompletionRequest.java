package cloud.isaura.llama3.model.ollama;

public record CompletionRequest(String model,
                                String system,
                                String prompt,
                                Options options,
                                String format,
                                Boolean stream) {
}
