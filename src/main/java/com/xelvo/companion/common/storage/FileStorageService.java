package com.xelvo.companion.common.storage;

import com.xelvo.companion.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg", "image/png", "image/webp"
    );

    private final Path uploadRoot;

    public FileStorageService(@Value("${app.storage.upload-dir:./uploads}") String uploadDir) {
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    public String storeCompanionPhoto(Long companionId, MultipartFile file) {
        validate(file);

        try {
            Path companionDirectory = uploadRoot.resolve("companions").resolve(companionId.toString());
            Files.createDirectories(companionDirectory);

            String originalName = StringUtils.cleanPath(
                    file.getOriginalFilename() == null ? "photo" : file.getOriginalFilename()
            );

            String extension = extensionOf(originalName);
            String fileName = UUID.randomUUID() + extension;

            Path destination = companionDirectory.resolve(fileName).normalize();

            if (!destination.startsWith(companionDirectory)) {
                throw new BusinessException("Invalid file name");
            }

            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/companions/" + companionId + "/" + fileName;
        } catch (IOException ex) {
            throw new BusinessException("Unable to store image");
        }
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("Image file is required");
        }

        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new BusinessException("Only JPG, PNG and WEBP images are allowed");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new BusinessException("Image size cannot exceed 5 MB");
        }
    }

    private String extensionOf(String name) {
        int index = name.lastIndexOf('.');
        if (index < 0) {
            throw new BusinessException("Image extension is required");
        }
        return name.substring(index).toLowerCase();
    }
}
