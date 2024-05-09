package cloud.isaura.llama3.model.ollama;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import org.jboss.logging.Logger;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.time.Duration;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;

public class OllamaClient {

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES)
            .create();

    private final OllamaApi ollamaApi;

    private static final Logger LOGGER = Logger.getLogger("OllamaClient");


    public OllamaClient(String baseUrl, Duration timeout) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .callTimeout(timeout)
                .connectTimeout(timeout)
                .readTimeout(timeout)
                .writeTimeout(timeout)
                .build();
        LOGGER.debug("baseUrl "+baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(GSON))
                .build();

        ollamaApi = retrofit.create(OllamaApi.class);
    }

    public ChatResponse chat(ChatRequest request) {
        LOGGER.debug("request "+request);
        try {
            retrofit2.Response<ChatResponse> retrofitResponse
                    = ollamaApi.chat(request).execute();

            if (retrofitResponse.isSuccessful()) {
                return retrofitResponse.body();
            } else {
                LOGGER.error(retrofitResponse.message());
                LOGGER.error(retrofitResponse.errorBody().string());
                throw new Exception(retrofitResponse.message());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public CompletionResponse generate(CompletionRequest request) {
        LOGGER.debug("request "+request);
        try {
            retrofit2.Response<CompletionResponse> retrofitResponse
                    = ollamaApi.completion(request).execute();

            if (retrofitResponse.isSuccessful()) {
                return retrofitResponse.body();
            } else {
                LOGGER.error(retrofitResponse.message());
                LOGGER.error(retrofitResponse.errorBody().string());
                throw new Exception(retrofitResponse.message());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
