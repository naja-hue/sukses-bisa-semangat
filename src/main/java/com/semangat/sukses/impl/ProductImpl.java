package com.semangat.sukses.impl;

import com.semangat.sukses.DTO.ProductDTO;
import com.semangat.sukses.model.Admin;
import com.semangat.sukses.model.Product;
import com.semangat.sukses.repository.ProductRepository;
import com.semangat.sukses.repository.AdminRepository;
import com.semangat.sukses.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AdminRepository adminRepository;  // Menambahkan dependency AdminRepository

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getAllProductsByAdmin(Long adminId) {
        List<Product> products = productRepository.findByAdminId(adminId);  // Asumsi ada metode findByAdminId
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductDTO> getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(this::convertToDTO);
    }

    @Override
    public ProductDTO addProduct(Long idAdmin, ProductDTO productDTO) {
        Optional<Admin> admin = adminRepository.findById(idAdmin);  // Mencari Admin berdasarkan ID
        if (admin.isPresent()) {
            Product product = convertToEntity(productDTO);  // Mengonversi DTO ke entitas
            product.setAdmin(admin.get());  // Menetapkan objek Admin ke produk
            Product savedProduct = productRepository.save(product);  // Menyimpan produk yang baru
            return convertToDTO(savedProduct);  // Mengonversi entitas yang disimpan ke DTO
        } else {
            throw new RuntimeException("Admin not found with ID: " + idAdmin);  // Menangani jika Admin tidak ditemukan
        }
    }


    @Override
    public Optional<ProductDTO> updateProduct(Long id, Long adminId, ProductDTO productDTO) throws IOException {
        if (productRepository.existsById(id)) {
            Optional<Admin> admin = adminRepository.findById(adminId);
            if (admin.isPresent()) {
                Product product = convertToEntity(productDTO);  // Mengonversi DTO ke entitas
                product.setId(id);  // Set ID produk yang akan diupdate
                product.setAdmin(admin.get());  // Set objek Admin ke produk

                Product updatedProduct = productRepository.save(product);  // Menyimpan produk yang diupdate
                return Optional.of(convertToDTO(updatedProduct));  // Mengembalikan produk dalam bentuk DTO
            } else {
                throw new IOException("Admin tidak ditemukan dengan ID: " + adminId);
            }
        } else {
            throw new IOException("Produk tidak ditemukan dengan ID: " + id);
        }
    }


    @Override
    public void deleteProduct(Long id) throws IOException {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new IOException("Product not found with ID: " + id);
        }
    }

    // Convert Product entity to ProductDTO
    private ProductDTO convertToDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getAdmin().getId(),  // Mengirimkan objek Admin langsung
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getStock()
        );
    }

    // Convert ProductDTO to Product entity
    private Product convertToEntity(ProductDTO productDTO) {
        Optional<Admin> admin = adminRepository.findById(productDTO.getAdmin());  // Mencari Admin berdasarkan ID dari ProductDTO
        if (admin.isPresent()) {
            return new Product(
                    productDTO.getId(),
                    admin.get(),
                    productDTO.getName(),
                    productDTO.getPrice(),
                    productDTO.getDescription(),
                    productDTO.getStock()
            );
        } else {
            throw new RuntimeException("Admin not found with ID: " + productDTO.getAdmin());  // Menangani kasus jika Admin tidak ditemukan
        }
    }
}
