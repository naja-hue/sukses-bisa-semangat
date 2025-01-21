package com.semangat.sukses.impl;

import com.semangat.sukses.DTO.ProductDTO;
import com.semangat.sukses.model.Admin;
import com.semangat.sukses.model.Product;
import com.semangat.sukses.repository.ProductRepository;
import com.semangat.sukses.repository.AdminRepository;
import com.semangat.sukses.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.json.JSONObject;

@Service
public class ProductImpl implements ProductService {

    private static final String BASE_URL = "https://s3.lynk2.co/api/s3";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getAllProductsByAdmin(Long adminId) {
        List<Product> products = productRepository.findByAdminId(adminId);
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
        Optional<Admin> admin = adminRepository.findById(idAdmin);
        if (admin.isPresent()) {
            Product product = convertToEntity(productDTO);
            product.setAdmin(admin.get());
            Product savedProduct = productRepository.save(product);
            return convertToDTO(savedProduct);
        } else {
            throw new RuntimeException("Admin not found with ID: " + idAdmin);
        }
    }

    @Override
    public Optional<ProductDTO> updateProduct(Long id, Long adminId, ProductDTO productDTO) throws IOException {
        if (productRepository.existsById(id)) {
            Optional<Admin> admin = adminRepository.findById(adminId);
            if (admin.isPresent()) {
                Product product = convertToEntity(productDTO);
                product.setId(id);
                product.setAdmin(admin.get());

                Product updatedProduct = productRepository.save(product);
                return Optional.of(convertToDTO(updatedProduct));
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

    public String uploadFoto(MultipartFile multipartFile) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String base_url = BASE_URL + "/absenMasuk";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", multipartFile.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(base_url, HttpMethod.POST, requestEntity, String.class);

        return extractFileUrlFromResponse(response.getBody());
    }

    private String extractFileUrlFromResponse(String responseBody) {
        // Contoh parsing jika respons berupa JSON
        JSONObject json = new JSONObject(responseBody);
        return json.getJSONObject("data").getString("url_file");
    }

    private ProductDTO convertToDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getAdmin().getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getStock(),
                product.getImageURL()
        );
    }

    private Product convertToEntity(ProductDTO productDTO) {
        Optional<Admin> admin = adminRepository.findById(productDTO.getAdmin());
        if (admin.isPresent()) {
            return new Product(
                    productDTO.getId(),
                    admin.get(),
                    productDTO.getName(),
                    productDTO.getPrice(),
                    productDTO.getDescription(),
                    productDTO.getStock(),
                    productDTO.getImageURL()
            );
        } else {
            throw new RuntimeException("Admin not found with ID: " + productDTO.getAdmin());
        }
    }
}
