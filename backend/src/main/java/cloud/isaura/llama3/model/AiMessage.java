package cloud.isaura.llama3.model;

public class AiMessage
{
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public AiMessage(String text) {
        this.text = text;
    }

    public static AiMessage from(String text) {
        return new AiMessage(text);
    }

    @Override
    public String toString() {
        return "AiMessage{" +
                "text='" + text + '\'' +
                '}';
    }
}
