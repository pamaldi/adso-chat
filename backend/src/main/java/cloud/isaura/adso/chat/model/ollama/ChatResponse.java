package cloud.isaura.adso.chat.model.ollama;

public record ChatResponse( String model,
        String createdAt,
        Message message, Boolean done,
        Integer promptEvalCount,
        Integer evalCount) {
}
