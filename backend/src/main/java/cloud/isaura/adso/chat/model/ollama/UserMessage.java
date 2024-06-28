package cloud.isaura.adso.chat.model.ollama;

public class UserMessage
{
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "UserMessage{" +
                "text='" + text + '\'' +
                '}';
    }
}
