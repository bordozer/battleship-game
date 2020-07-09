package com.bordozer.battleship.multiplayer.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController()
@RequestMapping("/api/health-check")
public class HealthCheckController {

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HealthCheckDto> healthCheck() {
        LOGGER.info("Health check is called");
        return new ResponseEntity<>(HealthCheckDto.of(), HttpStatus.OK);
    }

    @Getter
    private static class HealthCheckDto {
        private final String value = "OK";

        private static HealthCheckDto of() {
            return new HealthCheckDto();
        }
    }
}
