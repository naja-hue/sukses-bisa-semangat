package com.semangat.sukses.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.semangat.sukses.DTO.ProductDTO;
import com.semangat.sukses.model.Product;
import com.semangat.sukses.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> productDTOs  = productService.getAllProducts();
        return ResponseEntity.ok(productDTOs);
    }

    @GetMapping("/getAllByAdmin/{idAdmin}")
    public ResponseEntity<List<Product>> getAllProductsByAdmin(@PathVariable Long idAdmin) {
        List<Product> productDTOs  = productService.getAllProductsByAdmin(idAdmin);
        return ResponseEntity.ok(productDTOs );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product>  getProductById(@PathVariable Long id) {
        Optional<Product> product = productService. getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add/{idAdmin}")
    public ResponseEntity<ProductDTO> addProduct(
            @PathVariable Long idAdmin,
            @RequestParam("product") String productJson,
            @RequestParam("file") MultipartFile file) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        ProductDTO productDTO = objectMapper.readValue(productJson, ProductDTO.class);

        String imageURL = productService.uploadFoto(file);
        productDTO.setImageURL(imageURL);

        ProductDTO savedProduct = productService.addProduct(idAdmin, productDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @RequestParam Long idAdmin,
            @RequestParam(required = false) MultipartFile file,
            @RequestParam String product) throws IOException {

        // Parsing JSON produk
        ObjectMapper objectMapper = new ObjectMapper();
        ProductDTO productDTO = objectMapper.readValue(product, ProductDTO.class);

        // Jika ada file, upload dan tambahkan URL ke DTO
        if (file != null && !file.isEmpty()) {
            String imageURL = productService.editUploadFoto(id, file);
            productDTO.setImageURL(imageURL);
        }

        // Update produk
        ProductDTO updatedProduct = productService.updateProduct(id, idAdmin, productDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) throws IOException {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}