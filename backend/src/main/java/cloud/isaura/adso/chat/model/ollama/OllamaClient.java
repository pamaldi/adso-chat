package cloud.isaura.adso.chat.model.ollama;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.smallrye.mutiny.Multi;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import org.jboss.logging.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    public Multi<CompletionResponse> getDataStream(CompletionRequest request) {
        return Multi.createFrom().emitter(emitter -> {
            ollamaApi.streamingCompletion(request).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Jsonb jsonb = JsonbBuilder.create();
                        try (InputStream is = response.body().byteStream();
                             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                CompletionResponse completionResponse = jsonb.fromJson(line, CompletionResponse.class);
                                emitter.emit(completionResponse);
                            }
                            emitter.complete();
                        } catch (IOException e) {
                            emitter.fail(e);
                        }
                    } else {
                        emitter.fail(new RuntimeException("Server contacted but unsuccessful response"));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    emitter.fail(t);
                }
            });
        });
    }

    public Multi<String> getDataStreamString(CompletionRequest request) {
        return Multi.createFrom().emitter(emitter -> {
            ollamaApi.streamingCompletion(request).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Jsonb jsonb = JsonbBuilder.create();
                        try (InputStream is = response.body().byteStream();
                             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                CompletionResponse completionResponse = jsonb.fromJson(line, CompletionResponse.class);
                                LOGGER.debug("completion response "+completionResponse);
                                emitter.emit(completionResponse.response());
                            }
                            emitter.complete();
                        } catch (IOException e) {
                            emitter.fail(e);
                        }
                    } else {
                        emitter.fail(new RuntimeException("Server contacted but unsuccessful response"));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    emitter.fail(t);
                }
            });
        });
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
