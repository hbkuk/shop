package com.shop.core.address.presentation;

import com.shop.core.address.application.AddressService;
import com.shop.core.address.presentation.dto.AddressRequest;
import com.shop.core.address.presentation.dto.AddressResponse;
import com.shop.core.auth.domain.LoginUser;
import com.shop.core.auth.presentation.AuthenticationPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
public class AddressController {
    private AddressService addressService;

    @PostMapping("/addresses")
    public ResponseEntity<AddressResponse> create(@RequestBody AddressRequest request,
                                                  @AuthenticationPrincipal LoginUser loginUser) {
        AddressResponse response = addressService.create(request, loginUser);
        return ResponseEntity.created(URI.create("/addresses/" + response.getId())).body(response);
    }

    @GetMapping("/addresses/{id}")
    public ResponseEntity<AddressResponse> findById(@PathVariable Long id,
                                                    @AuthenticationPrincipal LoginUser loginUser) {
        AddressResponse response = addressService.findById(id, loginUser);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/addresses/{id}")
    public ResponseEntity<AddressResponse> update(@PathVariable Long id,
                                                  @RequestBody AddressRequest request,
                                                  @AuthenticationPrincipal LoginUser loginUser) {
        AddressResponse response = addressService.update(AddressRequest.mergeAddressId(id, request), loginUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressResponse>> findAll(@AuthenticationPrincipal LoginUser loginUser) {
        List<AddressResponse> responses = addressService.findAll(loginUser);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/addresses/default/{id}")
    public ResponseEntity<Void> updateDefault(@PathVariable Long id,
                                              @AuthenticationPrincipal LoginUser loginUser) {
        addressService.updateDefaultAddress(id, loginUser);
        return ResponseEntity.ok().build();
    }
}
