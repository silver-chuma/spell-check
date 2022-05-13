package com.app.spellcheck;

import constants.ResponseStatus;
import dto.SpellCheckDTO;
import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.rules.RuleMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vertx.java.core.json.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class SpellingCheckerController {

    @Autowired
    AppVerticle appVerticle;


    @RequestMapping(value = {"/spell_check"},
            method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<?> checkWords(@RequestBody SpellCheckDTO spellCheck) throws Throwable {

        System.out.println("To spell....");

        SpellCheckDTO spellCheckDTO = new SpellCheckDTO();

        spellCheckDTO.setSentence(spellCheck.getSentence());

        JLanguageTool langTool = new JLanguageTool(new AmericanEnglish());

        List<RuleMatch> matches = null;
        try {
            matches = langTool.check(spellCheck.getSentence());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String message = "";
        List <String> suggestion = new ArrayList<>();
        List <String> error = new ArrayList<>();

        for (RuleMatch match : matches) {
            error.add("Potential error exists at line " +
                    match.getEndLine() + ", column " +
                    match.getColumn() + " : " + match.getMessage());

            System.out.println("Suggested correction: " +
                    match.getSuggestedReplacements());

            message = match.getMessage();
            if(!match.getSuggestedReplacements().isEmpty()){
                match.getSuggestedReplacements().forEach(item->{
                  suggestion.add("Please add: " + item);
                });
            }
        }

        spellCheckDTO.setError(error);
        spellCheckDTO.setSuggestion(suggestion);
        if(error.isEmpty()){
            spellCheckDTO.setMessage("Looks nice :)");
            spellCheckDTO.setCode(ResponseStatus.CORRECT.getCode());
            spellCheckDTO.setStatus(ResponseStatus.CORRECT.getMessage());
        }else{
            spellCheckDTO.setMessage(message);
            spellCheckDTO.setCode(ResponseStatus.NOT_CORRECT.getCode());
            spellCheckDTO.setStatus(ResponseStatus.NOT_CORRECT.getMessage());
        }


        return new ResponseEntity<>(spellCheckDTO,HttpStatus.OK);

//        return appVerticle.getIo().sockets().emit("echo", new ResponseEntity<>(spellCheckDTO,HttpStatus.OK));


    }


}
