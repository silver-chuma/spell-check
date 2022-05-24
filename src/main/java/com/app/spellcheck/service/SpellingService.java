package com.app.spellcheck.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class SpellingService {

    private static Set<String> words = new HashSet<>();

    public SpellingService() {
        System.out.println("Init Dictionary...");
        ClassLoader cl = getClass().getClassLoader();
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(this.getClass().getResource("/Brian.dic").openStream()));
            words = reader.lines().collect(Collectors.toSet());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkSpellingInDictionary(String word) {
        if (word != null) {
            return words.contains(word.toLowerCase());
        } else {
            return false;
        }
    }

    public static List<String> suggestSimilarWords(String word) {
        List<String> suggestedWords = Optional.ofNullable(
                words.stream().filter(w -> (w.indexOf(word) >= 0 && w.lastIndexOf(word) >= 0)).collect(Collectors.toList())
        ).orElse(Collections.emptyList());
        Collections.sort(suggestedWords);
        return suggestedWords;
    }


}
