package com.shop.core.product.presentation;

import com.shop.common.auth.AuthenticationPrincipal;
import com.shop.common.domain.auth.LoginUser;
import com.shop.core.product.application.ProductService;
import com.shop.core.product.presentation.dto.ProductRequest;
import com.shop.core.product.presentation.dto.ProductResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<ProductResponse> registerProduct(@RequestBody ProductRequest request,
                                                           @AuthenticationPrincipal LoginUser loginUser) {
        ProductResponse response = productService.registerProduct(request, loginUser);
        return ResponseEntity.created(URI.create("/products/" + response.getId())).build();
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long productId,
                                                    @AuthenticationPrincipal LoginUser loginUser) {
        ProductResponse response = productService.findById(productId, loginUser);
        return ResponseEntity.ok(response);
    }
}
