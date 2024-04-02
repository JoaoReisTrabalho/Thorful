package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/characters")
public class CharacterController {

    private final String SWAPI_BASE_URL = "https://swapi.dev/api/people/";

    @GetMapping(produces = "application/json")
    public String getAllCharacters() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(SWAPI_BASE_URL, String.class);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public String getCharacterById(@PathVariable("id") int id) {
        String url = SWAPI_BASE_URL + id + "/";
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);
    }

    @GetMapping("/names")
    public List<String> getAllCharacterNames() {
        RestTemplate restTemplate = new RestTemplate();
        String allCharacters = restTemplate.getForObject(SWAPI_BASE_URL, String.class);

        // Use regex to extract character names
        List<String> characterNames = new ArrayList<>();
        Pattern pattern = Pattern.compile("\"name\":\"(.*?)\"");
        Matcher matcher = pattern.matcher(allCharacters);
        while (matcher.find()) {
            characterNames.add(matcher.group(1));
        }

        return characterNames;
    }

    @GetMapping(value = "/name/{name}", produces = "application/json")
    public Object getCharacterByName(@PathVariable("name") String name) {
        RestTemplate restTemplate = new RestTemplate();
        String allCharacters = restTemplate.getForObject(SWAPI_BASE_URL, String.class);

        // Split the response into individual character entries
        String[] characters = allCharacters.split("\\{\"name\":");

        // Find the character whose name matches the requested name
        for (String character : characters) {
            if (character.startsWith("\"" + name + "\"")) {
                character = character.substring(0, character.length() -1);
                character= "{\"name\":" + character;
                return character;
            }
        }

        return "Character not found";
    }
}