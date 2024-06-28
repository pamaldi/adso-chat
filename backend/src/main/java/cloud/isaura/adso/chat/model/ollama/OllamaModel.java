package cloud.isaura.adso.chat.model.ollama;

import cloud.isaura.adso.chat.utils.ValidationUtils;

public enum OllamaModel {
    LLAMA3("llama3"), LLLAMA370B("llama3:70b");

    private final String name;

    OllamaModel(String name) {
        this.name = ValidationUtils.ensureNotBlank(name, "modelName");
    }

    public String getName() {
        return name;
    }
}
