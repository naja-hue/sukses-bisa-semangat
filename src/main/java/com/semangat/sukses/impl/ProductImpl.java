package com.semangat.sukses.impl;

import com.semangat.sukses.DTO.ProductDTO;
import com.semangat.sukses.model.Product;
import com.semangat.sukses.repository.ProductRepository;
import com.semangat.sukses.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductDTO> getAllProducts() {
        // Mengambil semua produk dari database
        List<Product> products = productRepository.findAll();

        // Mengonversi Product ke ProductDTO
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductDTO> getProductById(Long id) {
        // Mencari produk berdasarkan ID
        Optional<Product> product = productRepository.findById(id);
        return product.map(this::convertToDTO);  // Jika produk ditemukan, konversi ke ProductDTO
    }

    @Override
    public ProductDTO addProduct(Product product) {
        // Menyimpan produk baru ke database
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);  // Mengembalikan ProductDTO yang telah disimpan
    }

    @Override
    public Optional<ProductDTO> updateProduct(Long id, Product product) {
        // Mengecek apakah produk dengan ID yang diberikan ada di database
        if (productRepository.existsById(id)) {
            // Memperbarui produk yang sudah ada
            product.setId(id);
            Product updatedProduct = productRepository.save(product);
            return Optional.of(convertToDTO(updatedProduct));  // Mengembalikan produk yang diperbarui dalam bentuk DTO
        } else {
            return Optional.empty();  // Jika tidak ditemukan produk dengan ID tersebut
        }
    }

    @Override
    public void deleteProduct(Long id) {
        // Menghapus produk berdasarkan ID
        productRepository.deleteById(id);
    }

    // Fungsi untuk mengonversi Product ke ProductDTO
    private ProductDTO convertToDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImage(),  // Menyertakan gambar
                product.getDescription(),
                product.getStock()
        );
    }
}
