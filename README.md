# Jackson HOWTO 

##Write a custom deserializer which works with different *types* of objects:    

Resolution: extend ```com.fasterxml.databind.deser.ContextualDeserializer```.
  
For example if you want to deserialize enum objects case insensitive 

(i.e. both JSON strings *{"val1"}* and *{"VaL1"}* get deserialized to *MyEnum.VAL1*) 

and do not want write per-enum deserializers like ```MyEnumOneDeserializer```, ```MyEnumTwoDeserializer``` then 
annotate corresponding enum with ```@JsonDeserialize(using = CaseInsensitiveEnumDeserializer.class)```

See ```my.howtows.jackson.serialization.enumdeserializer.CaseInsensitiveEnumDeserializer``` and corresponding test class for details.


# Java 8 HOWTO 

##Delayed function executor

Tries to execute function several times. If execution fails (exception is thrown) waits specified delays and re-executes
 
    final int[] i = {0}; 
    Integer result = new DelayedExecutor<>(() -> {
      i[0]++;
      System.out.println("i = " + i[0]);
      if (i[0] == 3) {
         return i[0];
       }
       throw new RuntimeException("" + i[0]);
     }, TimeUnit.SECONDS, 1, 5, 10).execute();
   
    System.out.println("result = " + result);

