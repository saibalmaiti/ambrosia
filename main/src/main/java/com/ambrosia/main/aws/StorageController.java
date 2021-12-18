package com.ambrosia.main.aws;

import com.ambrosia.main.aws.service.StorageService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/storage")
public class StorageController {
    private StorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam(value = "file") MultipartFile file) {
        return ResponseEntity.ok(storageService.uploadFile(file));
    }

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam(value = "file") String fileName) {
        byte[] data = storageService.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .body(resource);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFile(@RequestParam(value="file") String fileName) {
        return ResponseEntity.ok(storageService.deleteFile(fileName));
    }
}
