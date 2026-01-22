package krishnan.billingsoftware.service.imp;

import krishnan.billingsoftware.Repository.CategoryRepository;
import krishnan.billingsoftware.Repository.ItemRepository;
import krishnan.billingsoftware.entity.CategoryEntity;
import krishnan.billingsoftware.entity.ItemEntity;
import krishnan.billingsoftware.io.ItemRequest;
import krishnan.billingsoftware.io.ItemResponse;
import krishnan.billingsoftware.service.ItemService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemResponse add(ItemRequest request, MultipartFile file) {

        ItemEntity newItem= convertToEntity(request);
        CategoryEntity existingCategory =categoryRepository.findByCategoryId(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found "+request.getCategoryId()));
        newItem.setCategory(existingCategory);
        newItem.setImgUrl(existingCategory.getImgUrl());
        newItem=itemRepository.save(newItem);
        return null;
    }

    private ItemEntity convertToEntity(ItemRequest request) {
        return null;
    }

    private ItemResponse convertToResponse(ItemRequest newItem) {
        return null;
    }

    @Override
    public List<ItemResponse> fetchItems() {
        return List.of();
    }

    @Override
    public void deleteItem(String itemId) {

    }
}
