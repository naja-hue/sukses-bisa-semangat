package com.semangat.sukses.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.semangat.sukses.DTO.ProductDTO;
import com.semangat.sukses.exception.NotFoundException;
import com.semangat.sukses.model.Admin;
import com.semangat.sukses.model.Product;
import com.semangat.sukses.repository.AdminRepository;
import com.semangat.sukses.repository.ProductRepository;
import com.semangat.sukses.service.ProductService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductImpl implements ProductService {

    private static final String BASE_URL = "https://s3.lynk2.co/api/s3";
    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public ProductImpl(ProductRepository productRepository, AdminRepository adminRepository) {
        this.productRepository = productRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getAllProductsByAdmin(Long idAdmin) {
        return productRepository.findByAdminId(idAdmin);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public ProductDTO addProduct(Long idAdmin, ProductDTO productDTO) {
        Admin admin = adminRepository.findById(idAdmin)
                .orElseThrow(() -> new NotFoundException("Admin not found"));

        Product product = new Product();
        product.setAdmin(admin);
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setStock(productDTO.getStock());
        product.setImageURL(productDTO.getImageURL());

        Product savedProduct = productRepository.save(product);

        ProductDTO result = new ProductDTO();
        result.setId(savedProduct.getId());
        result.setName(savedProduct.getName());
        result.setPrice(savedProduct.getPrice());
        result.setDescription(savedProduct.getDescription());
        result.setStock(savedProduct.getStock());
        result.setImageURL(savedProduct.getImageURL());

        return result;
    }


    @Override
    public ProductDTO updateProduct(Long id, Long idAdmin,ProductDTO productDTO) throws IOException {
       Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product tidak ditemukan"));

        Admin admin = adminRepository.findById(idAdmin)
                .orElseThrow(() -> new NotFoundException("Admin dengan ID " + idAdmin + " tidak ditemukan"));

        existingProduct.setAdmin(admin);
        existingProduct.setName(productDTO.getName());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setStock(productDTO.getStock());

        Product updatedProduct = productRepository.save(existingProduct);

        ProductDTO result = new ProductDTO();
        result.setId(updatedProduct.getId());
        result.setName(updatedProduct.getName());
        result.setPrice(updatedProduct.getPrice());
        result.setDescription(updatedProduct.getDescription());
        result.setStock(updatedProduct.getStock());

        return result;
    }

    @Override
    public String uploadFoto(MultipartFile file) throws IOException {
        String uploadUrl = BASE_URL + "/uploadFoto";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(uploadUrl, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return extractFileUrlFromResponse(response.getBody());
        } else {
            throw new IOException("Failed to upload file: " + response.getStatusCode());
        }
    }

    @Override
    public String editUploadFoto(Long id, MultipartFile file) throws IOException {
        String editUrl = BASE_URL + "/editUploadFoto";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());
        body.add("fileId", id.toString());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(editUrl, HttpMethod.PUT, requestEntity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return extractFileUrlFromResponse(response.getBody());
        } else {
            throw new IOException("Failed to update file: " + response.getStatusCode());
        }
    }

    private String extractFileUrlFromResponse(String responseBody) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse = mapper.readTree(responseBody);
        JsonNode dataNode = jsonResponse.path("data");
        return dataNode.path("url_file").asText();
    }

    @Override
    public void deleteProduct(Long id) throws IOException {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new IOException("Product not found with ID: " + id);
        }
    }
}