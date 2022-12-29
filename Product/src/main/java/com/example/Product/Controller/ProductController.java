package com.example.Product.Controller;

import com.example.Product.Service.ProductService;
import com.example.Product.dto.ProductResponse;
import com.example.Product.dto.ProductRequest;
import com.example.Product.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest) {
        productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }
    @PutMapping("/{id}")
    public Product updateproduct(@RequestBody Product product, @PathVariable("id") String id)
    {
        return productService.updateproduct(product , id);
    }
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable ("id") String id)
    {
        productService.deleteproduct(id);
    }

}