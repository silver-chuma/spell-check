package com.app.spellcheck;

import com.app.spellcheck.service.SpellingService;
import constants.ResponseStatus;
import dto.SpellCheckDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class SpellingCheckerController {

    @Autowired
    SpellingService spellingService;


    @RequestMapping(value = {"/spell_check"},
            method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<?> checkWords(@RequestBody SpellCheckDTO spellCheck) throws Throwable {

        System.out.println("To spell....");

        SpellCheckDTO spellCheckDTO = new SpellCheckDTO();

        spellCheckDTO.setSentence(spellCheck.getSentence());

        String dictFound = spellingService.checkSpellingInDictionary(spellCheck.getSentence()) ? "FOUND" : "NOT_FOUND";
        List<String> suggestion = spellingService.suggestSimilarWords(spellCheck.getSentence());

        spellCheckDTO.setSuggestion(suggestion);
        if(dictFound.equalsIgnoreCase("FOUND")){
            spellCheckDTO.setMessage("Looks nice :)");
            spellCheckDTO.setCode(ResponseStatus.CORRECT.getCode());
            spellCheckDTO.setStatus(ResponseStatus.CORRECT.getMessage());
        }else{
            spellCheckDTO.setMessage("Words not found in dictionary!");
            spellCheckDTO.setCode(ResponseStatus.NOT_CORRECT.getCode());
            spellCheckDTO.setStatus(ResponseStatus.NOT_CORRECT.getMessage());
        }


        return new ResponseEntity<>(spellCheckDTO,HttpStatus.OK);

//        return appVerticle.getIo().sockets().emit("echo", new ResponseEntity<>(spellCheckDTO,HttpStatus.OK));


    }


}
