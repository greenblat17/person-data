package com.greenblat.naumentask.controllers;

import com.greenblat.naumentask.services.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/people")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping("/search")
    public String search() {
        return "people/search";
    }

    @GetMapping("/search/age")
    public String searchAgeByPersonName(@RequestParam("query") String queryName, Model model) {
        model.addAttribute("name", queryName);
        model.addAttribute("age", personService.getPersonByName(queryName));

        return "people/search";
    }

    @GetMapping("/statistics/name-by-max-age")
    public String nameByMaxAge(Model model) {
        model.addAttribute("namesWithMaxAge", personService.getNamesWithMaxAge());

        return "people/max-age";
    }

    @GetMapping("/statistics/count")
    public String getCountAllName(Model model) {
        model.addAttribute("people", personService.getAllPerson());

        return "people/count";
    }
}
