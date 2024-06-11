package cloud.isaura.llama3.model.ollama;

import cloud.isaura.llama3.utils.ValidationUtils;

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
