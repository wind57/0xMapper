# OxMapper

A declarative pojo to pojo mapper. POJO in  the sense of a Java bean : assuming default constructor, setters and getters. 
If you don't have those, this isn't for you, at least for the moment. 
I might expand this to immutable types or go beyond public classes. In the current release this is not supported.

## Examples

```java

@Getter
@Setter
@Accessors(chain = true)
public class PersonIn {
    // getters/setters + constructor omitted
    private String firstName;
    private String lastName;

}

@Getter
@Setter
public class PersonOut {
    // getters/setters + constructor omitted
    private String firstName;
    private String lastName;
}





// define a so-called MappingRegistrar that captures the types and names of the fields 
// the mapping should be performed against.
    
public class PersonMapper {

    public static void main(String[] args) {

        PersonIn in = new PersonIn()
            .setFirstName("John")
            .setLastName("Smith");

        MappingRegistrar<PersonIn, PersonOut> mapper = new MappingRegistrar<>(){};
        mapper.withMapping("firstName", String.class)
              .withMapping("lastName", String.class);

        PersonOut out = mapper.transform(in);
        System.out.println(out.getFirstName()); // John
        System.out.println(out.getLastName()); // Smith

    }

}
```

