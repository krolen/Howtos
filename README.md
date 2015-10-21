# Jackson HOWTO 

##Write a custom deserializer which works with different *types* of objects:    

Resolution: extend ```com.fasterxml.databind.deser.ContextualDeserializer```.
  
For example if you want to deserialize enum objects case insensitive 

(i.e. both JSON strings *{"val1"}* and *{"VaL1"}* get deserialized to *MyEnum.VAL1*) 

and do not want write per-enum deserializers like ```MyEnumOneDeserializer```, ```MyEnumTwoDeserializer``` then 
annotate corresponding enum with ```@JsonDeserialize(using = CaseInsensitiveEnumDeserializer.class)```

See ```my.howtows.jackson.serialization.enumdeserializer.CaseInsensitiveEnumDeserializer``` and corresponding test class for details.
