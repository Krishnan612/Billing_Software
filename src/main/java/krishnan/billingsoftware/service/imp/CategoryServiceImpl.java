package krishnan.billingsoftware.service.imp;

import krishnan.billingsoftware.Repository.CategoryRepository;
import krishnan.billingsoftware.entity.CategoryEntity;
import krishnan.billingsoftware.io.CategoryRequest;
import krishnan.billingsoftware.io.CategoryResponse;
import krishnan.billingsoftware.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final krishnan.billingsoftware.service.storage.StorageService storageService;

    @Override
    public CategoryResponse add(CategoryRequest request) {
        return add(request, null);
    }

    @Override
    public CategoryResponse add(CategoryRequest request, org.springframework.web.multipart.MultipartFile file) {
        CategoryEntity newCategory = convertToEntity(request);
        // handle multipart file
        if (file != null && !file.isEmpty()) {
            String url = storageService.store(file);
            newCategory.setImgUrl(url);
        }
        // handle base64 image embedded in JSON (data URI or raw base64)
        if (request.getImgUrl() != null && !request.getImgUrl().isBlank()) {
            String data = request.getImgUrl();
            String contentType = null;
            String base64Part = data;
            if (data.startsWith("data:")) {
                // format: data:image/png;base64,AAA...
                int semi = data.indexOf(';');
                int comma = data.indexOf(',');
                if (semi > 0) contentType = data.substring(5, semi);
                if (comma > 0) base64Part = data.substring(comma + 1);
            }
            try {
                byte[] bytes = java.util.Base64.getDecoder().decode(base64Part);
                String url = storageService.store(bytes, contentType != null ? contentType : "image/png");
                newCategory.setImgUrl(url);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid base64 image data", e);
            }
        }

        categoryRepository.save(newCategory);
        return convertToResponse(newCategory);
    }

    @Override
    public List<CategoryResponse> read() {
       return categoryRepository.findAll()
                .stream()
                .map(categoryEntity -> convertToResponse(categoryEntity))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String categoryId) {
      CategoryEntity existingCategory =  categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not Found"+categoryId));
      categoryRepository.delete(existingCategory);
    }

    private CategoryResponse convertToResponse(CategoryEntity newCategory) {
       return CategoryResponse.builder()
                .categoryId(newCategory.getCategoryId())
                .name(newCategory.getName())
                .description(newCategory.getDescription())
                .bgcolor(newCategory.getBgcolor())
                .imgUrl(newCategory.getImgUrl())
               .createdAt(newCategory.getCreatedAt())
               .updatedAt(newCategory.getUpdatedAt())
                .build();
    }

    private CategoryEntity convertToEntity(CategoryRequest request) {
        return CategoryEntity.builder()
                .categoryId(UUID.randomUUID().toString())
                .name(request.getName())
                .description(request.getDescription())
                .bgcolor(request.getBgcolor())
                .imgUrl(request.getImgUrl())
                .build();
    }
}
