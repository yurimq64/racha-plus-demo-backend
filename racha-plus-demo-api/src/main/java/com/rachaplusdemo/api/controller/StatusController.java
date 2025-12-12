package com.rachaplusdemo.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class StatusController {

    @GetMapping
    public String status() {
        return "A API do Racha+ está rodando com sucesso!";
    }
}
