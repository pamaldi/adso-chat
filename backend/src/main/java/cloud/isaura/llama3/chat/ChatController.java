package cloud.isaura.llama3.chat;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

import java.time.LocalDate;
@Path("/chat")
public class ChatController
{
    private static final Logger LOGGER = Logger.getLogger("ChatController");

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public void chat(ChatRequest chatRequest)
    {
        LOGGER.debug("chat request "+chatRequest);
    }
}
