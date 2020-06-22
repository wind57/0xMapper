package com.github.er.mapper.fruit;

import com.github.er.mapper.MappingRegistrar;
import com.github.er.mapper.Registrar.Builder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FruitTest {

    // only map if this one is a berry, since this one is an Apple, nothing gets mapped
    @Test
    public void testNoMatch() {
        FruitIn fruitIn = new FruitIn()
            .setColor("GREEN")
            .setName("Apple");

        MappingRegistrar<FruitIn, FruitOut> fruitMapper = new MappingRegistrar<>() {};
        // name mapper
        fruitMapper.withBuilder(new Builder<>().sameFieldNames("name")
                                               .sameFieldTypes(String.class)
                                               .inputPredicate("berry"::equals));

        FruitOut fruitOut = fruitMapper.transform(fruitIn);

        Assertions.assertNotNull(fruitOut);
        Assertions.assertNull(fruitOut.getColor());
        Assertions.assertNull(fruitOut.getName());
    }

    // the opposite of the previous test
    @Test
    public void testWithAMatch() {
        FruitIn fruitIn = new FruitIn()
            .setColor("GREEN")
            .setName("Apple");

        MappingRegistrar<FruitIn, FruitOut> fruitMapper = new MappingRegistrar<>() {};

        // name mapper
        fruitMapper.withBuilder(new Builder<>().sameFieldNames("name")
                                               .sameFieldTypes(String.class)
                                               .inputPredicate("Apple"::equals));

        // color mapper
        fruitMapper.withBuilder(new Builder<String, Color>().sameFieldNames("color")
                                                            .inputFieldType(String.class)
                                                            .outputFieldType(Color.class)
                                                            .mapper(Color::valueOf));

        FruitOut fruitOut = fruitMapper.transform(fruitIn);

        Assertions.assertNotNull(fruitOut);
        Assertions.assertSame(Color.GREEN, fruitOut.getColor());
        Assertions.assertEquals("Apple", fruitOut.getName());
    }

    @Test
    public void testWithPredicate() {
        FruitIn fruitIn = new FruitIn()
            .setColor("GREEN")
            .setShape("almostRound")
            .setName("Apple");

        MappingRegistrar<FruitIn, FruitOut> fruitMapper = new MappingRegistrar<>() {};

        // shape/form mapper
        fruitMapper.withBuilder(new Builder<String, String>().inputFieldName("shape")
                                                             .outputFieldName("form")
                                                             .sameFieldTypes(String.class)
                                                             .inputPredicate(x -> x.contains("Round")));

        FruitOut fruitOut = fruitMapper.transform(fruitIn);

        Assertions.assertNotNull(fruitOut);
        Assertions.assertEquals("almostRound", fruitOut.getForm());
    }

}
