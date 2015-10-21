package my.howtows.jackson.serialization.enumdeserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import java.io.IOException;

/**
 * Created by kkulagin on 10/21/2015.
 */
public class CaseInsensitiveEnumDeserializer <T extends Enum> extends JsonDeserializer<T> implements ContextualDeserializer {

  private Class<T> enumClass;

  public CaseInsensitiveEnumDeserializer(Class<T> enumClass) {
    this.enumClass = enumClass;
  }

  protected CaseInsensitiveEnumDeserializer() {
  }

  @Override
  public JsonDeserializer createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
    JavaType type = ctxt.getContextualType();
    Class<?> parameterSource = type.getRawClass();
    if (Enum.class.isAssignableFrom(parameterSource)) {
      Class<? extends Enum> source = (Class<? extends Enum>) parameterSource;
      return new CaseInsensitiveEnumDeserializer<>(source);
    }
    return null;
  }

  @Override
  public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    return (T) Enum.valueOf(enumClass, p.getValueAsString().toUpperCase());
  }
}
