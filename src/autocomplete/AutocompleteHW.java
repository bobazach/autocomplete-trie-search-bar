package edu.emory.cs.trie.autocomplete;

import edu.emory.cs.trie.TrieNode;

import java.util.*;

public class AutocompleteHW extends Autocomplete<List<List<String>>> {
    // Stores the candidate lists for prefixes, updated when a candidate is picked or introduced
    private List<List<String>> prefixCandidates = new ArrayList<>();

    public AutocompleteHW(String dict_file, int max) {
        super(dict_file, max);
    }

    @Override
    public List<String> getCandidates(String prefix) {
        prefix = prefix.trim();
        List<String> candidates = new ArrayList<>();
        // Checks if candidates were already chosen for prefix, adds to the front if so
        for (List<String> list : prefixCandidates) {
            if (prefix.equals(list.get(0))) {
                candidates = list.subList(1, list.size());
            }
        }
        // Generate candidate list using BFS
        Queue<TrieNode> queue = new LinkedList<>();
        TrieNode begin = find(prefix);

        if (begin == null) {
            return candidates;
        }

        queue.add(begin);
        while (!queue.isEmpty()) {
            TrieNode node = queue.poll();
            Map<Character, TrieNode> map = node.getChildrenMap();
            // Check if node is end of word
            if (node.isEndState()) {
                String word = "";
                while (node.getParent() != null) {
                    word = node.getKey() + word;
                    node = node.getParent();
                }
                // Check that word isn't already added
                if (!candidates.contains(word)) {
                    candidates.add(word);
                }
            }
            // Add and sort children to queue using a list
            List<Character> childrenNodes = new ArrayList<Character>();
            for (Map.Entry<Character, TrieNode> curr : map.entrySet()) {
                childrenNodes.add(curr.getKey());
            }

            Collections.sort(childrenNodes);

            for (Character child : childrenNodes) {
                queue.add(map.get(child));
            }
        }

        if (candidates.size() <= getMax()) {
            return candidates;
        } else {
            return candidates.subList(0, getMax());
        }
    }

    @Override
    public void pickCandidate(String prefix, String candidate) {
        prefix = prefix.trim();
        boolean prefixExists = false;
        boolean candidateExists = false;
        // Check if candidate list for prefix already exists
        for (List<String> list : prefixCandidates) {
            if (prefix.equals(list.get(0))) {
                prefixExists = true;
                // Check if candidate is in list, shift to front if so
                for (String word : list) {
                    if (word.equals(candidate)) {
                        candidateExists = true;
                        String temp = word;
                        list.remove(candidate);
                        list.add(1, temp);
                        break;
                    }
                }
                if (!candidateExists) {
                    list.add(1, candidate);
                }
            }
        }
        // Creates candidate list for prefix if prefix does not exist
        if (!prefixExists) {
            List<String> newPrefixList = new ArrayList<String>();
            newPrefixList.add(prefix);
            newPrefixList.add(candidate);
            prefixCandidates.add(newPrefixList);
        }
        // Add candidate into trie/dictionary
        put(candidate, prefixCandidates);
    }

    public static void main(String[] args) {
        String dict_file = "src/main/resources/dict.txt";
        int max = 20;
        Autocomplete<?> trie = new AutocompleteHW(dict_file, max);

        System.out.println(trie.getCandidates("   shoc   "));
    }
}