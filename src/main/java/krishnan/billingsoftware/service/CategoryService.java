package krishnan.billingsoftware.service;

import krishnan.billingsoftware.io.CategoryRequest;
import krishnan.billingsoftware.io.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse add(CategoryRequest request);
    CategoryResponse add(CategoryRequest request, org.springframework.web.multipart.MultipartFile file);

    List<CategoryResponse> read();
    void delete(String categoryId);
}
