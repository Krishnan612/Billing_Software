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
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping("/admin/categories")
//    @PostMapping(consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse addCategorey(@RequestPart(value = "category", required = false) String categoryJson,
                                        @RequestParam(value = "name", required = false) String name,
                                        @RequestParam(value = "description", required = false) String description,
                                        @RequestParam(value = "bgcolor", required = false) String bgcolor,
                                        @org.springframework.web.bind.annotation.RequestPart(value = "image", required = false) org.springframework.web.multipart.MultipartFile image,
                                        @org.springframework.web.bind.annotation.RequestPart(value = "file", required = false) org.springframework.web.multipart.MultipartFile file){
        CategoryRequest request = null;
        if (categoryJson != null && !categoryJson.isBlank()){
            try{
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                request = mapper.readValue(categoryJson, CategoryRequest.class);
            }catch (com.fasterxml.jackson.core.JsonProcessingException e){
                throw new org.springframework.web.server.ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid category JSON", e);
            }
        } else {
            // Build from individual fields
            if (name == null || name.isBlank()){
                throw new org.springframework.web.server.ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required field 'name'");
            }
            request = CategoryRequest.builder()
                    .name(name)
                    .description(description)
                    .bgcolor(bgcolor)
                    .build();
        }

        // prefer image param; fall back to file param
        org.springframework.web.multipart.MultipartFile chosenFile = (image != null && !image.isEmpty()) ? image : file;
        return categoryService.add(request, chosenFile);
    }

    @PostMapping(consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse addCategoreyJson(@RequestBody CategoryRequest request){
        return categoryService.add(request);
    }

    @PostMapping(path = "/form-json", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse addCategoreyFormJson(@RequestPart("category") String categoryJson,
                                                 @RequestPart(value = "file", required = false) org.springframework.web.multipart.MultipartFile file){
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            CategoryRequest request = mapper.readValue(categoryJson, CategoryRequest.class);
            return categoryService.add(request, file);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e){
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid category JSON", e);
        }
    }
    @GetMapping
    public List<CategoryResponse> fetchCategories(){
        return categoryService.read();
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/admin/categories/{categoryId}")
    public void remove(@PathVariable String categoryId){
        try{
            categoryService.delete(categoryId);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }
    }

}
