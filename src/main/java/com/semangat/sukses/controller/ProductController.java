package com.semangat.sukses.controller;

import com.semangat.sukses.DTO.ProductDTO;
import com.semangat.sukses.model.Product;
import com.semangat.sukses.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products") // Semua endpoint di bawah akan diakses melalui /api/products
public class ProductController {

    @Autowired
    private ProductService productService;

    // Endpoint untuk mendapatkan semua produk
    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();  // Mengembalikan List<ProductDTO>
    }

    // Mendapatkan produk berdasarkan ID
    @GetMapping("/{id}")
    public Optional<ProductDTO> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);  // Mengembalikan Optional<ProductDTO>
    }

    // Endpoint untuk menambah produk
    @PostMapping("/add")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody Product product) {
        ProductDTO savedProduct = productService.addProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    // Endpoint untuk memperbarui produk
    @PutMapping("/edit/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Optional<ProductDTO> updatedProduct = productService.updateProduct(id, product);
        return updatedProduct.map(productDTO -> new ResponseEntity<>(productDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint untuk menghapus produk
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}
