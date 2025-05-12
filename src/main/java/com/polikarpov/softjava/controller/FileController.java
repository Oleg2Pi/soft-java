package com.polikarpov.softjava.controller;

import com.polikarpov.softjava.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/findMin")
    public ResponseEntity<?> findMin(@RequestParam String filePath, @RequestParam Integer n) {
        if (n <= 0) {
            return ResponseEntity.badRequest().body("N не может быть меньше или равным 0.");
        }

        return ResponseEntity.ok(fileService.findMinNumber(filePath, n));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
