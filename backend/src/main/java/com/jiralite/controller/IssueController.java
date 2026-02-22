package com.jiralite.controller;

import com.jiralite.service.IssueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

    @GetMapping
    public ResponseEntity<String> getAll() {
        return ResponseEntity.ok("liste des issues");
    }
}