package com.samana.controller;

import com.samana.dto.HostRequest;
import com.samana.dto.HostResponse;
import com.samana.service.HostService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hosts")
@CrossOrigin("*")
public class HostController {

    @Autowired
    private HostService hostService;


    // =========================================================
    // CREATE HOST
    // =========================================================

    @PostMapping
    public HostResponse createHost(
            @Valid @RequestBody HostRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        return hostService.createHost(
                request,
                email
        );
    }


    // =========================================================
    // GET ALL HOSTS
    // =========================================================

    @GetMapping
    public List<HostResponse> getAllHosts() {

        return hostService.getAllHosts();
    }


    // =========================================================
    // GET HOST BY ID
    // =========================================================

    @GetMapping("/{id}")
    public HostResponse getHostById(
            @PathVariable Long id) {

        return hostService.getHostById(id);
    }


    // =========================================================
    // UPDATE HOST
    // =========================================================

    @PutMapping("/{id}")
    public HostResponse updateHost(
            @PathVariable Long id,
            @Valid @RequestBody HostRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        return hostService.updateHost(
                id,
                request,
                email
        );
    }


    // =========================================================
    // DELETE HOST
    // =========================================================

    @DeleteMapping("/{id}")
    public String deleteHost(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();

        hostService.deleteHost(
                id,
                email
        );

        return "Host deleted successfully";
    }
}