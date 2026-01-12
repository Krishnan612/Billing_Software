package krishnan.billingsoftware.controller;

import krishnan.billingsoftware.io.CategoryRequest;
import krishnan.billingsoftware.io.CategoryResponse;
import krishnan.billingsoftware.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping(consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse addCategorey(@RequestParam("name") String name,
                                        @RequestParam(value = "description", required = false) String description,
                                        @RequestParam(value = "bgcolor", required = false) String bgcolor,
                                        @org.springframework.web.bind.annotation.RequestPart(value = "image", required = false) org.springframework.web.multipart.MultipartFile image){
        CategoryRequest request = CategoryRequest.builder()
                .name(name)
                .description(description)
                .bgcolor(bgcolor)
                .build();
        return categoryService.add(request, image);
    }

    @PostMapping(consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse addCategoreyJson(@RequestBody CategoryRequest request){
        return categoryService.add(request);
    }
    @GetMapping
    public List<CategoryResponse> fetchCategories(){
        return categoryService.read();
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{categoryId}")
    public void remove(@PathVariable String categoryId){
        try{
            categoryService.delete(categoryId);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }
    }

}
