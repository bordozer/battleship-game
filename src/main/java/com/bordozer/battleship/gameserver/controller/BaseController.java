package com.bordozer.battleship.gameserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

@Controller
public class BaseController {

    @GetMapping("/")
    public String portalPage(final Model model) {
        model.addAttribute("playerId", UUID.randomUUID().toString());
        return "index";
    }
}
