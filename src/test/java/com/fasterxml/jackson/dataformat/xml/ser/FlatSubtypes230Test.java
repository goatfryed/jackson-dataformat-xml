package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Goatfryed
 */
public class FlatSubtypes230Test {

    public static class Person {
        public final List<Hobby> hobbies = new ArrayList<>();
    }

    @JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    @JsonSubTypes({
        @JsonSubTypes.Type(value=Reading.class, name = "reading"),
        @JsonSubTypes.Type(value=Biking.class, name = "biking")
    })
    public interface Hobby {

    }

    public static class Reading implements Hobby {
        public final String favoriteBook;

        public Reading(String favoriteBook) {
            this.favoriteBook = favoriteBook;
        }
    }

    public static class Biking implements Hobby {
        public final String bikeType;

        public Biking(String bikeType) {
            this.bikeType = bikeType;
        }
    }

    // [dataformat-xml#230]
    @Test
    public void testFlatSubtypes() throws IOException {

        Person person = new Person();
        person.hobbies.add(new Reading("moby dick"));
        person.hobbies.add(new Biking("mountain bike"));

        String actual = new XmlMapper().writeValueAsString(person);

        String expected = "<Person><hobbies>" +
                "<reading>" +
                    "<favoriteBook>moby dick</favoriteBook>" +
                "</reading>" +
                "<biking>" +
                    "<bikeType>mountain bike></bikeType>" +
                "</biking>" +
            "</hobbies></Person>";

        assertEquals(expected, actual);
    }
}
