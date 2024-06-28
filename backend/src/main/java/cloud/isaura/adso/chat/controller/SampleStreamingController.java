package cloud.isaura.adso.chat.controller;

import io.smallrye.mutiny.Multi;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestSseElementType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Path("/sample/streaming")
public class SampleStreamingController
{
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();


    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestSseElementType(MediaType.TEXT_PLAIN)
    public Multi<String> stream() {
        return Multi.createFrom().emitter(emitter -> {
            executorService.submit(() -> {
                try {
                    for (int i = 1; i <= 10; i++) {
                        emitter.emit("Line " + i);
                        Thread.sleep(1000); // Simulate delay
                    }
                    emitter.complete();
                } catch (InterruptedException e) {
                    emitter.fail(e);
                }
            });
        });
    }
}
