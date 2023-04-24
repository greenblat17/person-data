package com.greenblat.naumentask.controllers;

import com.greenblat.naumentask.services.PersonService;
import com.greenblat.naumentask.services.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final PersonService personService;
    private final StatisticsService statisticsService;

    @GetMapping("/name-by-max-age")
    public String nameByMaxAge(Model model) {
        model.addAttribute("peopleWithMaxAge", personService.getNamesWithMaxAge());

        return "statistics/max-age";
    }

    @GetMapping("/count")
    public String countAllName(@RequestParam(value = "page", defaultValue = "0") Integer page, Model model) {
        model.addAttribute("prevPage", page - 1);
        model.addAttribute("nextPage", page + 1);
        model.addAttribute("size", statisticsService.getCountStatisticsInDb());
        model.addAttribute("fullStatistics", statisticsService.getStatisticsPeople(page));
        return "statistics/count";
    }

    @GetMapping("/full")
    public String fullStatisticsByName(@RequestParam("name") String name,
                                       Model model) {
        model.addAttribute("name", name);
        model.addAttribute("personFullStatistics", statisticsService.getFullStatisticsByName(name));

        return "statistics/stats-by-name";
    }

}
