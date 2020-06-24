# 0xMapper

A declarative pojo to pojo mapper. POJO in  the sense of a Java bean : assuming default constructor, setters and getters. 
If you don't have those, this isn't for you, at least for the moment. 
I might expand this to immutable types or go beyond public classes. In the current release this is not supported.

## Simple Example

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

## More interesting example

```java

@Getter
@Setter
@Accessors(chain = true)
public class In<T extends Number> {

    private List<T> list;

    private String userAge;
    private String color;

}


@Getter
@Setter
public class Out<R extends Number> {

    private List<R> list;

    private int age;
    private int color;

}



public class Mapper {

    public static void main(String[] args) {

        In<Long> in = new In<Long>()
            .setList(List.of(1L))
            .setUserAge("57")
            .setColor("blue");

        MappingRegistrar<In<Long>, Out<Integer>> registrar = new MappingRegistrar<>() {};

        // 1. age mapping : from "userAge" to "age" (String to int, according to the "mapper")
        // only map if Predicate (digits only) matches the given input
        registrar.withBuilder(
            new Builder<String, Integer>().inputFieldName("userAge")
                                          .outputFieldName("age")
                                          .inputFieldType(String.class)
                                          .outputFieldType(int.class)
                                          .inputPredicate(Pattern.compile("\\d+").asPredicate())
                                          .mapper(Integer::parseInt)
        );

        // 2. color mapping: color to color (String to int, according to the "mapper")
        // only map if output matches the output Predicate (at least 4 digits)

        registrar.withBuilder(
            new Builder<String, Integer>().sameFieldNames("color")
                                          .inputFieldType(String.class)
                                          .outputFieldType(int.class)
                                          .mapper(String::length)
                                          .outputPredicate(x -> x >= 4)
        );

        // 3. map a generic List
        // inputFieldType and outputFieldType both accept a Type
        registrar.withBuilder(
            new Builder<List<Long>, List<Integer>>().sameFieldNames("list")
                                                    .inputFieldType(new TypeToken<List<Integer>>() {}.getType())
                                                    .outputFieldType(List.class)
                                                    .mapper(longList -> longList.stream()
                                                                                .map(Long::intValue)
                                                                                .collect(Collectors.toList()))
        );

        Out<Integer> out = registrar.transform(in);
        System.out.println(out.getAge()); // 57
        System.out.println(out.getColor()); // 4
        System.out.println(out.getList()); // [1]

    }

}

```
## This seems cumbersome, why would I want that?

The creation (and validation of existing methods/fields) is done only once, per each instance 
of MappingRegistrar. This means that you can create the MappingRegistrar only once and call `transform`
as many times as needed, without only additional overhead.

You can think of the MappingRegistrar as a "file" (statically build and compiled only once) that defines 
all the needed mappings and their rules. 


## Speed comparison

For SimpleExample above, here are some results (compared to Datus, direct and map-struct):


```
Benchmark                Mode  Cnt   Score   Error  Units
SimpleExample.datus      avgt    5   9.989 ± 2.230  ns/op
SimpleExample.direct     avgt    5   6.346 ± 1.351  ns/op
SimpleExample.mapStruct  avgt    5   5.298 ± 1.040  ns/op
SimpleExample.mapper     avgt    5  19.377 ± 0.850  ns/op
```

The direct mappings and map-struct are on par (for obvious reasons) and datus comes next, only then comes mapper.

I lose 2x to datus and around 3-4x to other tools.

If we switch the tests to the more complex example, I still lose, but by a lower margin:

```
Benchmark                     Mode  Cnt    Score    Error  Units
InterestingExample.datus      avgt    5  164.806 ± 54.982  ns/op
InterestingExample.direct     avgt    5  135.487 ±  4.550  ns/op
InterestingExample.mapStruct  avgt    5  135.299 ±  8.550  ns/op
InterestingExample.mapper     avgt    5  198.957 ±  8.532  ns/op
```

