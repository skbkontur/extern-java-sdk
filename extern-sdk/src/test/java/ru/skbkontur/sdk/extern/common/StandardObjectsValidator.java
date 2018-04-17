package ru.skbkontur.sdk.extern.common;

import ru.skbkontur.sdk.extern.model.Link;

import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.*;

public class StandardObjectsValidator {
    public static void validateId(UUID id) {
        assertEquals("Id is wrong!", StandardValues.ID, id.toString());
    }

    public static void validateEmptyList(List list, String listName) {
        assertNotNull(listName + " must not be null!", list);
        assertTrue(listName + " must be empty!", list.isEmpty());
    }

    public static void validateNotEmptyList(List list, String listName) {
        assertNotNull(listName + " must not be null!", list);
        assertFalse(listName + " must not be empty!", list.isEmpty());
    }

    public static void validateLink(Link link) {
        assertNotNull("Link must not be null!", link);
        assertEquals("Rel is wrong!", "string", link.getRel());
        assertEquals("Href is wrong!", "string", link.getHref());
        assertEquals("Name is wrong!", "string", link.getName());
        assertEquals("Title is wrong!", "string", link.getTitle());
        assertEquals("Profile is wrong!", "string", link.getProfile());
        assertTrue("Templated is wrong!", link.getTemplated());
    }
}
