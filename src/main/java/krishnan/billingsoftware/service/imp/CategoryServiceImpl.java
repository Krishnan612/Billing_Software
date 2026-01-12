package krishnan.billingsoftware.service.imp;

import krishnan.billingsoftware.Repository.CategoreyRepository;
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
    private final CategoreyRepository categoreyRepository;
    private final krishnan.billingsoftware.service.storage.StorageService storageService;

    @Override
    public CategoryResponse add(CategoryRequest request) {
        return add(request, null);
    }

    @Override
    public CategoryResponse add(CategoryRequest request, org.springframework.web.multipart.MultipartFile file) {
        CategoryEntity newCategory = convertToEntity(request);
        // handle file
        if (file != null && !file.isEmpty()) {
            String url = storageService.store(file);
            newCategory.setImgUrl(url);
        }
        categoreyRepository.save(newCategory);
        return convertToResponse(newCategory);
    }

    @Override
    public List<CategoryResponse> read() {
       return categoreyRepository.findAll()
                .stream()
                .map(categoryEntity -> convertToResponse(categoryEntity))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String categoryId) {
      CategoryEntity existingCategory =  categoreyRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not Found"+categoryId));
      categoreyRepository.delete(existingCategory);
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
