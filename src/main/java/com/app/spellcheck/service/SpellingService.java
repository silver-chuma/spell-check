package com.app.spellcheck.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SpellingService {

    private Set<String> words = new HashSet<>();

    public SpellingService() {
        ClassLoader cl = getClass().getClassLoader();
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(this.getClass().getResource("/Brian.dic").openStream()));
            words = reader.lines().collect(Collectors.toSet());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkSpellingInDictionary(String word) {
        if (word != null) {
            return words.contains(word.toLowerCase());
        } else {
            return false;
        }
    }

    public List<String> suggestSimilarWords(String word) {
        List<String> suggestedWords = Optional.ofNullable(
                words.stream().filter(w -> (w.indexOf(word) >= 0 && w.lastIndexOf(word) >= 0)).collect(Collectors.toList())
        ).orElse(Collections.emptyList());
        Collections.sort(suggestedWords);
        return suggestedWords;
    }


}
