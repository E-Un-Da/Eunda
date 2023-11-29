package com.hanghae.eunda.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RecordController {

    @PostMapping("/cards/{id}/records")
    public ResponseEntity<String> createRecord() {
        return null;
    }

    @GetMapping("/cards/{id}/records")
    public ResponseEntity<String> getRecords() {
        return null;
    }

    @GetMapping("/records/{recordId}")
    public ResponseEntity<String> getRecord() {
        return null;
    }

    @PutMapping("/records/{recordId}")
    public ResponseEntity<String> updateRecord() {
        return null;
    }

    @DeleteMapping("/records/{recordId}")
    public ResponseEntity<String> deleteRecord() {
        return null;
    }
}
