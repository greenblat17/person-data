package com.greenblat.naumentask.controllers;

import com.greenblat.naumentask.services.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping("/search")
    public String search() {
        return "persons/search";
    }

    @GetMapping("/search/age")
    public String searchAgeByPersonName(@RequestParam("query") String queryName, Model model) {
        model.addAttribute("age", personService.getAgeByName(queryName));

        return "persons/search";
    }

    @GetMapping("/name-by-max-age")
    public String nameByMaxAge(Model model) {
        model.addAttribute("namesWithMaxAge", personService.getNameWithMaxAge());

        return "persons/max-age";
    }
}
