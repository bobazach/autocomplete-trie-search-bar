package edu.emory.cs.trie.autocomplete;

import edu.emory.cs.trie.TrieNode;

import java.util.*;

public class AutocompleteHWExtra extends Autocomplete<List<List<List<String>>>> {
    // Stores the candidate lists for prefixes, updated when a candidate is picked or introduced
    private List<List<List<String>>> prefixCandidatesAndFrequency = new ArrayList<>();
    // stores {"prefix", "candidate", "frequency"}

    public AutocompleteHWExtra(String dict_file, int max) {
        super(dict_file, max);
    }

    @Override
    public List<String> getCandidates(String prefix) {
        List<String> candidates = new ArrayList<>();
        // Checks if candidates were already chosen for prefix, adds to the front if so
        for (List<List<String>> list : prefixCandidatesAndFrequency) {
            if (prefix.equals(list.get(0).get(0))) {
                for(int i = 0; i < list.size(); i++){
                    candidates.add(list.get(i).get(1));
                }
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
        for (List<List<String>> list : prefixCandidatesAndFrequency) {
            if(prefix.equals(list.get(0).get(0))){
                prefixExists = true;
                // Check if candidate is in list
                int indexOfCandidate = 0;
                for(int i = 0; i < list.size(); i++){
                    if(list.get(i).get(1).equals(candidate)){
                        candidateExists = true;
                        indexOfCandidate = i;
                        // Update candidate's frequency
                        int oldFreq = Integer.parseInt(list.get(i).get(2));
                        String newFreq = Integer.toString(oldFreq + 1);
                        list.get(i).set(2, newFreq);
                        // Shift candidate's position in List
                        while(indexOfCandidate > 0){
                            if(Integer.parseInt(list.get(indexOfCandidate).get(2)) >= Integer.parseInt(list.get(indexOfCandidate - 1).get(2))){
                                Collections.swap(list, indexOfCandidate, indexOfCandidate - 1);
                                indexOfCandidate--;
                            }
                        }
                        break;
                    }
                }
                // Creates new list for candidate
                if(!candidateExists){
                    List<String> newCandidate = new ArrayList<>();
                    newCandidate.add(prefix);
                    newCandidate.add(candidate);
                    newCandidate.add("1");
                    list.add(newCandidate);
                }
            }
        }
        // Creates candidate list for prefix if prefix does not exist
        if (!prefixExists) {
            List<List<String>> newPrefixList = new ArrayList<>();
            List<String> newEntity = new ArrayList<>();
            newEntity.add(prefix);
            newEntity.add(candidate);
            newEntity.add("1");
            newPrefixList.add(newEntity);
            prefixCandidatesAndFrequency.add(newPrefixList);
        }
        // Add candidate into trie/dictionary
        put(candidate, prefixCandidatesAndFrequency);
    }

   public static void main(String[] args) {
       String dict_file = "src/main/resources/dict.txt";
       int max = 20;
       Autocomplete<?> trie = new AutocompleteHWExtra(dict_file, max);
//
       System.out.println(trie.getCandidates("apple"));
       trie.pickCandidate("apple", "applejack");
       trie.pickCandidate("apple", "appleroot");
       trie.pickCandidate("apple", "appleroot");
       trie.pickCandidate("apple", "appleroot");
       trie.pickCandidate("apple", "appleroot");
       trie.pickCandidate("apple", "applejack");
       trie.pickCandidate("apple", "applejack");
       trie.pickCandidate("apple", "applejack");
    //    System.out.println(trie.getCandidates("apple"));
       trie.pickCandidate("a b", "ab");
    //    System.out.println(trie.getCandidates("a b"));
       trie.pickCandidate("apple", "appleroot");
       trie.pickCandidate("apple", "applecart");
       trie.pickCandidate("apple", "applewife");
       trie.pickCandidate("apple", "applewood");
    //    System.out.println(trie.getCandidates("apple"));
    //    System.out.println(trie.getCandidates("a"));
    //    System.out.println(trie.getCandidates("apple"));
       trie.pickCandidate("a", "apple");
       trie.pickCandidate("apple", "applepie");
    //    System.out.println(trie.getCandidates("a"));
    //    System.out.println(trie.getCandidates("apple"));
   }

}