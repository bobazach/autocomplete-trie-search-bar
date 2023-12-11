package edu.emory.cs.trie;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class TrieQuiz extends Trie<Integer> {
    /**
     * PRE: this trie contains all country names as keys and their unique IDs as values
     * (e.g., this.get("United States") -> 0, this.get("South Korea") -> 1).
     * @param input the input string in plain text
     *              (e.g., "I was born in South Korea and raised in the United States").
     * @return the list of entities (e.g., [Entity(14, 25, 1), Entity(44, 57, 0)]).
     */
    List<Entity> getEntities(String input) {

        List<Entity> countries = new ArrayList<Entity>();

        if (input.length() == 0) {
            return countries;
        }

        // Checks all substrings beginning at each index in the input String to see if they're a country
        for (int beginIndex = 0; beginIndex < input.length(); beginIndex++) {
            for (int endIndex = beginIndex + 1; endIndex <= input.length(); endIndex++) {
                String possibleCountry = input.substring(beginIndex, endIndex);
                // Checks if any country begins with the substring, breaks out of the loop elsewise since there's no chance of beginning with a longer substring
                if (find(possibleCountry) != null) {
                    // Checks if the substring is a country, creates an Entity object if true
                    if (find(possibleCountry).isEndState()) {
                        countries.add(new Entity(beginIndex, endIndex, find(possibleCountry).getValue()));
                    }
                }else {
                    break;
                }
            }
        }

        return countries;
    }
}