package com.vicky.blog.controller;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vicky.blog.common.dto.search.SearchDTO;
import com.vicky.blog.common.dto.search.SearchResponse;
import com.vicky.blog.common.dto.search.SearchDTO.SearchBy;
import com.vicky.blog.common.dto.search.SearchDTO.SearchType;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.SearchService;
import com.vicky.blog.common.utility.UserIdExtracter;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/app/search")
public class SearchController {

    @Autowired
    private UserIdExtracter userIdExtracter;

    @Autowired
    private SearchService searchService;
    
    @GetMapping
    public ResponseEntity<SearchResponse> search(@RequestParam String query, @RequestParam SearchType type, 
        @RequestParam SearchBy searchBy, Principal principal, HttpServletRequest request) throws AppException {

        String userId = userIdExtracter.getUserId(principal);
        SearchDTO searchDTO = searchService.search(userId, query, type);
        SearchResponse response = new SearchResponse();
        response.setMessage("success");
        response.setPath(request.getServletPath());
        response.setStatus(HttpStatus.OK.value());
        response.setTime(LocalDateTime.now());
        response.setResults(searchDTO);

        return ResponseEntity.ok().body(response);
    }
}
