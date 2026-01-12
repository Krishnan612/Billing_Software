package krishnan.billingsoftware.service.storage;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileSystemStorageService implements StorageService {
    private final Path root = Paths.get("uploads").toAbsolutePath().normalize();

    public FileSystemStorageService() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage directory", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        // basic validation: only allow image content types
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("Invalid file type: only image files are allowed");
        }
        String original = file.getOriginalFilename();
        String ext = "";
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf('.'));
        }
        String filename = UUID.randomUUID().toString() + ext;
        Path target = root.resolve(filename);
        try {
            Files.copy(file.getInputStream(), target);
            // Return the public path relative to server root so it can be served by Spring static resource handler
            return "/uploads/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Override
    public String store(byte[] data, String contentType) {
        if (data == null || data.length == 0) {
            return null;
        }
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("Invalid content type: only image content types are allowed");
        }
        String ext = ".bin";
        if (contentType.contains("/")) {
            ext = "." + contentType.substring(contentType.indexOf('/') + 1);
            // clamp common exceptions
            if (ext.equalsIgnoreCase(".jpeg")) ext = ".jpg";
        }
        String filename = UUID.randomUUID().toString() + ext;
        Path target = root.resolve(filename);
        try {
            Files.write(target, data);
            return "/uploads/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store data", e);
        }
    }
}
