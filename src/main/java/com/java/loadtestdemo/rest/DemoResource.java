package com.java.loadtestdemo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "/delegate")
public class DemoResource {

    private RestTemplate restTemplate;

    @RequestMapping(path = "/demo", method = RequestMethod.GET)
    public String getDemoDelegate() {
        return this.restTemplate.getForObject("http://localhost:8080/demo", String.class);
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}