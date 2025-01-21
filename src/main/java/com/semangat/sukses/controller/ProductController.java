package com.semangat.sukses.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.semangat.sukses.DTO.ProductDTO;
import com.semangat.sukses.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject; nn   

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
        return productService.getAllProducts();
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
    public ResponseEntity<ProductDTO> addProduct(
            @PathVariable Long idAdmin,
            @RequestParam("productDTO") String productDTOJson,
            @RequestParam("file") MultipartFile file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ProductDTO productDTO = objectMapper.readValue(productDTOJson, ProductDTO.class);

            String fileUrl = uploadFoto(file);
            productDTO.setImageURL(fileUrl);

            ProductDTO savedProduct = productService.addProduct(idAdmin, productDTO);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @RequestParam("idAdmin") Long idAdmin,
            @RequestParam("productDTO") String productDTOJson,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            // Konversi JSON ke ProductDTO
            ObjectMapper objectMapper = new ObjectMapper();
            ProductDTO productDTO = objectMapper.readValue(productDTOJson, ProductDTO.class);

            // Jika file baru disertakan, unggah file dan perbarui URL gambar
            if (file != null && !file.isEmpty()) {
                String fileUrl = uploadFoto(file);
                productDTO.setImageURL(fileUrl);
            }

            // Update produk menggunakan service
            ProductDTO updatedProduct = productService.updateProduct(id, idAdmin, productDTO)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private String uploadFoto(MultipartFile multipartFile) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String base_url = "https://s3.lynk2.co/api/s3/absenMasuk";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(multipartFile.getBytes()) {
            @Override
            public String getFilename() {
                return multipartFile.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(base_url, HttpMethod.POST, requestEntity, String.class);

        return extractFileUrlFromResponse(response.getBody());
    }

    private String extractFileUrlFromResponse(String responseBody) {
        // Contoh parsing jika respons berupa JSON
        JSONObject json = new JSONObject(responseBody);
        return json.getJSONObject("data").getString("url_file");
    }
}
