package com.samana.service;

import com.samana.dto.HostRequest;
import com.samana.dto.HostResponse;
import com.samana.entity.Host;
import com.samana.entity.User;
import com.samana.repository.HostRepository;
import com.samana.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HostService {

    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private UserRepository userRepository;


    // =========================================================
    // CREATE HOST
    // =========================================================

    public HostResponse createHost(
            HostRequest request,
            String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));


        // Check role

        if (user.getRole() == null ||
                !user.getRole().name().equals("HOST")) {

            throw new RuntimeException(
                    "Only HOST users can create a host profile"
            );
        }


        // Check existing host profile

        if (hostRepository
                .findByUserId(user.getId())
                .isPresent()) {

            throw new RuntimeException(
                    "Host profile already exists"
            );
        }


        Host host = new Host();

        host.setUser(user);
        host.setBio(request.getBio());
        host.setPhone(request.getPhone());


        Host savedHost =
                hostRepository.save(host);


        return convertToResponse(savedHost);
    }


    // =========================================================
    // GET ALL HOSTS
    // =========================================================

    public List<HostResponse> getAllHosts() {

        return hostRepository
                .findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }


    // =========================================================
    // GET HOST BY ID
    // =========================================================

    public HostResponse getHostById(Long id) {

        Host host = hostRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Host not found"
                        ));


        return convertToResponse(host);
    }


    // =========================================================
    // UPDATE HOST
    // =========================================================

    public HostResponse updateHost(
            Long id,
            HostRequest request,
            String email) {

        Host host = hostRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Host not found"
                        ));


        User user = host.getUser();


        if (!user.getEmail().equals(email)) {

            throw new RuntimeException(
                    "You are not allowed to update this host"
            );
        }


        host.setBio(request.getBio());
        host.setPhone(request.getPhone());


        Host updated =
                hostRepository.save(host);


        return convertToResponse(updated);
    }


    // =========================================================
    // DELETE HOST
    // =========================================================

    public void deleteHost(
            Long id,
            String email) {

        Host host = hostRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Host not found"
                        ));


        User user = host.getUser();


        if (!user.getEmail().equals(email)) {

            throw new RuntimeException(
                    "You are not allowed to delete this host"
            );
        }


        hostRepository.delete(host);
    }


    // =========================================================
    // CONVERT
    // =========================================================

    private HostResponse convertToResponse(
            Host host) {

        User user = host.getUser();

        return new HostResponse(
                host.getId(),
                user.getId(),
                user.getName(),
                user.getEmail(),
                host.getBio(),
                host.getPhone()
        );
    }
}