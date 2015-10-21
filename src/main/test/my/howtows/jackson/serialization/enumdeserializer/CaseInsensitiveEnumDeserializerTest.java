package my.howtows.jackson.serialization.enumdeserializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by kkulagin on 10/21/2015.
 */
public class CaseInsensitiveEnumDeserializerTest {

  private ObjectMapper mapper = new ObjectMapper();

  @Test
  public void test() throws IOException {
    ObjectReader listTypeReader = mapper.readerFor(MyEnum.class);
    assertThat(MyEnum.VAL1, equalTo(listTypeReader.readValue("\"" + MyEnum.VAL1.name().toLowerCase() + "\"")));
    assertThat(MyEnum.VAL1, equalTo(listTypeReader.readValue("\"" + MyEnum.VAL1.name() + "\"")));
  }

  @Test
  public void testBean() throws IOException {
    TestBean testBean = new TestBean(MyEnum.VAL2);
    String jsonTestBean = mapper.writeValueAsString(testBean);
    System.out.println(jsonTestBean);
    TestBean readBean = mapper.readValue(jsonTestBean, TestBean.class);
    assertThat(testBean.myEnum, equalTo(readBean.myEnum));
  }

  @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
      setterVisibility = JsonAutoDetect.Visibility.NONE)
  private static class TestBean {
    private MyEnum myEnum;

    public TestBean(MyEnum myEnum) {
      this.myEnum = myEnum;
    }

    protected TestBean() {
    }
  }

  @JsonDeserialize(using = CaseInsensitiveEnumDeserializer.class)
  enum MyEnum {
    VAL1,
    VAL2
  }
}