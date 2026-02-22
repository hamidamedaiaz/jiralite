package com.jiralite.controller;

import com.jiralite.service.IssueService;

public class IssueController {
    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }
}
