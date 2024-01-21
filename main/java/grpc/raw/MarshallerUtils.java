package grpc.raw;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.grpc.MethodDescriptor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MarshallerUtils {
    public static ObjectMapper defaultMapper;

    static {
        JsonFactory jsonFactory = new JsonFactory();
        jsonFactory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        jsonFactory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
        defaultMapper = new ObjectMapper(jsonFactory);
        defaultMapper.disable(SerializationFeature.CLOSE_CLOSEABLE);
    }

    public static <T> MethodDescriptor.Marshaller<T> createMarshaller(Class<T> clz) {
        return new MethodDescriptor.Marshaller<T>() {

            @Override
            public InputStream stream(T value) {
                try {
                    return new ByteArrayInputStream(defaultMapper.writeValueAsBytes(value));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public T parse(InputStream stream) {
                try {
                    return defaultMapper.readValue(stream, clz);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

}
