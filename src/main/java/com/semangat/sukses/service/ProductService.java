package com.semangat.sukses.service;

import com.semangat.sukses.DTO.ProductDTO;
import com.semangat.sukses.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    // Mendapatkan semua produk dalam bentuk ProductDTO
    List<ProductDTO> getAllProducts();

    // Mendapatkan produk berdasarkan ID, mengembalikan ProductDTO
    Optional<ProductDTO> getProductById(Long id);

    // Menambahkan produk baru, menerima Product dan mengembalikan ProductDTO
    ProductDTO addProduct(Product product);

    // Memperbarui produk berdasarkan ID, menerima Product dan mengembalikan ProductDTO
    Optional<ProductDTO> updateProduct(Long id, Product product);

    // Menghapus produk berdasarkan ID
    void deleteProduct(Long id);
}
