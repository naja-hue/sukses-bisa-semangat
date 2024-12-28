package com.semangat.sukses.controller;

import com.semangat.sukses.DTO.ProductDTO;
import com.semangat.sukses.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();  // Mengembalikan List<ProductDTO>
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        Optional<ProductDTO> productDTO = productService.getProductById(id);
        return productDTO.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/getAllByAdmin/{idAdmin}")
    public ResponseEntity<List<ProductDTO>> getAllProductsByAdmin(@PathVariable Long idAdmin) {
        List<ProductDTO> productDTOs = productService.getAllProductsByAdmin(idAdmin);
        return ResponseEntity.ok(productDTOs);
    }

    @PostMapping("/add/{idAdmin}")
    public ResponseEntity<ProductDTO> addProduct(@PathVariable Long idAdmin, @RequestBody ProductDTO productDTO) {
        try {
            // Menetapkan adminId dari URL ke ProductDTO sebelum menyimpannya
            productDTO.setAdmin(idAdmin); // Set admin dari path ke DTO

            // Mengirimkan productDTO dengan ID admin ke service untuk disimpan
            ProductDTO savedProduct = productService.addProduct(idAdmin, productDTO);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping(value = "/edit/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @RequestParam Long idAdmin,
            @RequestBody ProductDTO productDTO) throws IOException {

        ProductDTO updatedProduct = productService.updateProduct(id, idAdmin, productDTO)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return ResponseEntity.ok(updatedProduct);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Menangani IOException jika produk tidak ditemukan
        }
    }
}
