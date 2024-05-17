package com.shop.core.product.application;

import com.shop.common.domain.auth.LoginUser;
import com.shop.common.exception.ErrorType;
import com.shop.core.product.domain.Product;
import com.shop.core.product.domain.ProductRepository;
import com.shop.core.product.domain.ProductStatus;
import com.shop.core.product.exception.DuplicateProductNameException;
import com.shop.core.product.exception.NonMatchingProductException;
import com.shop.core.product.exception.NotFoundProductException;
import com.shop.core.product.presentation.dto.ProductRequest;
import com.shop.core.product.presentation.dto.ProductResponse;
import com.shop.core.store.application.StoreService;
import com.shop.core.store.domain.Store;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final StoreService storeService;
    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse registerProduct(ProductRequest request, LoginUser loginUser) {
        verifyStoreByStoreManagerEmail(loginUser.getEmail());

        if (isAlreadyProductName(request.getName())) {
            throw new DuplicateProductNameException(ErrorType.DUPLICATE_PRODUCT_NAME);
        }

        Product product = request.toEntity(ProductStatus.AVAILABLE);
        productRepository.save(product);

        return ProductResponse.of(product);
    }

    public ProductResponse findById(Long productId, LoginUser loginUser) {
        Store store = verifyStoreByStoreManagerEmail(loginUser.getEmail());
        Product product = findProductById(productId);

        if (!store.isOwn(product)) {
            throw new NonMatchingProductException(ErrorType.NON_MATCHING_PRODUCT);
        }

        return ProductResponse.of(product);
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new NotFoundProductException(ErrorType.NOT_FOUND_PRODUCT));
    }

    private boolean isAlreadyProductName(String name) {
        return productRepository.findByName(name).isPresent();
    }

    private Store verifyStoreByStoreManagerEmail(String email) {
        return storeService.findByStoreManagerEmail(email);
    }
}
