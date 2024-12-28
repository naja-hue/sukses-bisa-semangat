package com.semangat.sukses.service;

import com.semangat.sukses.DTO.ProductDTO;
import com.semangat.sukses.model.Product;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    // Mendapatkan semua produk dalam bentuk List<ProductDTO>
    List<ProductDTO> getAllProducts();

    // Mendapatkan semua produk oleh admin berdasarkan idAdmin, mengembalikan List<ProductDTO>
    List<ProductDTO> getAllProductsByAdmin(Long idAdmin);

    // Mendapatkan produk berdasarkan ID dalam bentuk ProductDTO
    Optional<ProductDTO> getProductById(Long id);

    // Menambah produk baru, menerima ProductDTO dan idAdmin, mengembalikan ProductDTO
    ProductDTO addProduct(Long idAdmin, ProductDTO productDTO);

    // Memperbarui produk yang ada berdasarkan id dan idAdmin, mengembalikan ProductDTO
    public Optional<ProductDTO> updateProduct(Long id, Long adminId, ProductDTO productDTO) throws IOException;


    // Menghapus produk berdasarkan ID
    void deleteProduct(Long id) throws IOException;
}
