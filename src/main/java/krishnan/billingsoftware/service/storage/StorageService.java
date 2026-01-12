package krishnan.billingsoftware.service.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    /**
     * Stores the provided file on disk and returns the public URL path (relative) where it can be accessed.
     */
    String store(MultipartFile file);

    /**
     * Stores raw bytes with a content-type and returns the public URL path (relative) where it can be accessed.
     */
    String store(byte[] data, String contentType);
} 
