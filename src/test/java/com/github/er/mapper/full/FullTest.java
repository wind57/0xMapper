package com.github.er.mapper.full;

import com.github.er.mapper.MappingRegistrar;
import com.github.er.mapper.Registrar.Builder;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FullTest {

    @Test
    public void test() {

        Human human = new Human()
            .setAge("35")
            .setChildren(Map.of("boy", "David"))
            .setEyesColor("BROWN")
            .setFirstName("Eugene")
            .setLastName("Rabii")
            .setFun(List.of("soccer"))
            .setHome(new Home()
                         .setApNumber(109)
                         .setStreet("Franklin")
                         .setZip(new Zip().setZip("5757")));

        MappingRegistrar<Human, Dev> registrar = new MappingRegistrar<>() {};

        // 1. age mapper
        registrar.withBuilder(new Builder<String, Integer>().sameFieldNames("age")
                                                            .inputFieldType(String.class)
                                                            .outputFieldType(int.class)
                                                            .mapper(Integer::valueOf));

        // 2. children mapper
        registrar.withBuilder(new Builder<Map<String, String>, List<String>>()
                                  .inputFieldType(Map.class)
                                  .outputFieldType(List.class)
                                  .sameFieldNames("children")
                                  .mapper(map -> map
                                      .entrySet()
                                      .stream()
                                      .map(x -> x.getKey() + "=" + x.getValue())
                                      .collect(Collectors.toList())
                                  ));

        // 3. eyes mapper
        registrar.withBuilder(new Builder<String, Color>().sameFieldNames("eyesColor")
                                                          .inputFieldType(String.class)
                                                          .outputFieldType(Color.class)
                                                          .mapper(Color::valueOf));

        // 4. name mapper
        registrar.directly("name", String.class, h -> h.getFirstName().substring(0, 1) + h.getLastName().substring(0, 1));

        // 5. activities mapper
        registrar.withMapping("fun", "extraActivities", List.class);

        registrar.withBuilder(new Builder<Home, Address>().inputFieldType(Home.class)
                                                          .outputFieldType(Address.class)
                                                          .inputFieldName("home")
                                                          .outputFieldName("address")
                                                          .mapper(this.homeAddressMapper()::transform));
//

        Dev dev = registrar.transform(human);

        // 1. age assertions
        Assertions.assertSame(35, dev.getAge());
        // 2. children mapper
        Assertions.assertIterableEquals(List.of("boy=David"), dev.getChildren());
        // 3. eyes mapper
        Assertions.assertSame(Color.BROWN, dev.getEyesColor());
        // 4. name mapper
        Assertions.assertEquals("ER", dev.getName());
        // 5. activities mapper
        Assertions.assertEquals(List.of("soccer"), dev.getExtraActivities());

        Assertions.assertEquals("109", dev.getAddress().getApartment());
        Assertions.assertEquals("Franklin", dev.getAddress().getStreet());
        Assertions.assertEquals(5757, dev.getAddress().getZip());
    }

    private MappingRegistrar<Home, Address> homeAddressMapper() {

        MappingRegistrar<Home, Address> addressRegistrar = new MappingRegistrar<>() {};
        addressRegistrar.withMapping("street", String.class);

        addressRegistrar.withBuilder(new Builder<Integer, String>().inputFieldName("apNumber")
                                                                   .outputFieldName("apartment")
                                                                   .inputFieldType(int.class)
                                                                   .outputFieldType(String.class)
                                                                   .mapper(x -> x + ""));

        addressRegistrar.withBuilder(new Builder<Zip, Integer>().sameFieldNames("zip")
                                                                .inputFieldType(Zip.class)
                                                                .outputFieldType(int.class)
                                                                .mapper(x -> Integer.valueOf(x.getZip())));

        return addressRegistrar;
    }

}
