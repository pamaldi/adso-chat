package cloud.isaura.adso.chat.model.ollama;

public record CompletionResponse( String model,
                                  String createdAt, // Assumes a string format for date-time; consider using a more specific type like LocalDateTime if possible
                                  String response,
                                  Boolean done,
                                  Integer promptEvalCount,
                                  Integer evalCount) {
}
