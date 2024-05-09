package cloud.isaura.llama3.model.ollama;

import java.time.LocalDateTime;

public record ChatResponse( String model,
        String createdAt,
        Message message, Boolean done,
        Integer promptEvalCount,
        Integer evalCount) {
}
