package edu.emory.cs.trie;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrieQuizTest {
    @Test
    public void testGetEntities() {
        final List<String> L = List.of("Peru", "Japan", "United States", "Perun");
        TrieQuiz trie = new TrieQuiz();
        for (int i = 0; i < L.size(); i++)
            trie.put(L.get(i), i);

        // Test Cases
//        String input = "Peru";
//        List<Entity> entities = List.of(new Entity(0, 4, 0));

//        String input = "Peru Japan";
//        List<Entity> entities = List.of(new Entity(0, 4, 0), new Entity(5, 10, 1));

//        String input = "Perun";
//        List<Entity> entities = List.of(new Entity(0, 4, 0), new Entity(0, 5, 3));

//        String input = "Peru Japan United States";
//        List<Entity> entities = List.of(new Entity(0, 4, 0), new Entity(5, 10, 1), new Entity(11, 24, 2));

//        String input = "PeruUnited StatesJapan";
//        List<Entity> entities = List.of(new Entity(0, 4, 0), new Entity(4, 17, 2), new Entity(17, 22, 1));

//        String input = "aaaPeruaaa";
//        List<Entity> entities = List.of(new Entity(3, 7, 0));

//        String input = "aaaPerunaaa";
//        List<Entity> entities = List.of(new Entity(3, 7, 0), new Entity(3, 8, 3));

//        String input = "aaaPeruaaaUnited States";
//        List<Entity> entities = List.of(new Entity(3, 7, 0), new Entity(10, 23, 2));

//        String input = "foo";
//        List<Entity> entities = List.of();

//        String input = "";
//        List<Entity> entities = List.of();

        String input = "Perun";
        List<Entity> entities = List.of(new Entity(0, 4, 0), new Entity(0, 5, 3));

        Set<String> expected = entities.stream().map(Entity::toString).collect(Collectors.toSet());
        Set<String> actual = trie.getEntities(input).stream().map(Entity::toString).collect(Collectors.toSet());
        assertEquals(expected, actual);
    }
}