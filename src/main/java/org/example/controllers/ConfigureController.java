package org.example.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import org.example.service.CPULoad;
import org.example.service.MemoryLeak;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigureController {

    private final MemoryLeak.Leak leakProcess = new MemoryLeak.Leak(1, true);

    @Hidden
    @GetMapping(path = "/api/startLeak")
    public ResponseEntity<String> enableMemoryLeak() {
        leakProcess.setLeakFlagLeak(true);
        Thread thread = new Thread(leakProcess);
        thread.start();
        return ResponseEntity.ok("Memory Leak Enabled");
    }

    @Hidden
    @GetMapping(path = "/api/stopLeak")
    public ResponseEntity<String> disableMemoryLeak() {
        leakProcess.stopLeak();
        return ResponseEntity.ok("Memory Leak Disabled");
    }

    @Hidden
    @GetMapping(path = "/api/startCPULoad")
    public ResponseEntity<String> enableCPULoad() {
        CPULoad.CPUStartLoad(15);
        return ResponseEntity.ok("CPU load Enabled");
    }

    @Hidden
    @GetMapping(path = "/api/stopCPULoad")
    public ResponseEntity<String> disableCPULoad() {
        CPULoad.CPUStopLoad();
        return ResponseEntity.ok("CPU load Disabled");
    }
}
