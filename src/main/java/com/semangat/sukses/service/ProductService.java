package com.semangat.sukses.service;

import com.semangat.sukses.DTO.ProductDTO;
import com.semangat.sukses.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    List<Product> getAllProductsByAdmin(Long idAdmin);

    Optional<Product> getProductById(Long id);

    ProductDTO addProduct(Long idAdmin, ProductDTO productDTO);

    ProductDTO updateProduct(Long id, Long idAdmin, ProductDTO productDTO) throws IOException;

    String editUploadFoto(Long id, MultipartFile file) throws IOException;

    String uploadFoto(MultipartFile file) throws IOException;

    void deleteProduct(Long id) throws IOException;
}
