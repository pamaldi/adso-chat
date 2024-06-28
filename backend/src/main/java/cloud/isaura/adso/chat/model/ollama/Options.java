package cloud.isaura.adso.chat.model.ollama;

import java.util.List;

public record Options(Double temperature,
                      Integer topK,
                      Double topP,
                      Double repeatPenalty,
                      Integer seed,
                      Integer numPredict,
                      Integer numCtx,
                      List<String> stop) {
}
