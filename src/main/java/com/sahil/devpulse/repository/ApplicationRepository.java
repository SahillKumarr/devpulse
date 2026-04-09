package com.sahil.devpulse.repository;

import com.sahil.devpulse.model.Application;
import com.sahil.devpulse.model.enums.AppStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface ApplicationRepository extends JpaRepository<Application,Long> {
    // naam se app dhundho — login/register check ke liye
    Optional<Application> findByName(String name);

    // koi app is naam se already exist karta hai?
    boolean existsByName(String name);

    // status ke basis pe filter karo — e.g. sirf DOWN apps
    List<Application> findByStatus(AppStatus status);
}
