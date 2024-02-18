package com.vicky.blog.controller;

import java.time.LocalDateTime;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vicky.blog.common.dto.UniqueNameResponse;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.ProfileIdService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/app/unique")
public class UniqueNameController {

    private ProfileIdService profileIdService;
    
    @PostMapping("/{name}")
    public ResponseEntity<?> isUniqueName(@PathVariable String name, HttpServletRequest request) throws AppException {

        boolean isProfileIdExists = profileIdService.isProfileIdExists(name);
        UniqueNameResponse response = new UniqueNameResponse();
        response.setMessage(isProfileIdExists ? "ProfileId exists!" : "ProfileId not exists!");
        response.setStatus(HttpStatus.SC_OK);
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());

        return ResponseEntity.ok().body(response);
    }
}
