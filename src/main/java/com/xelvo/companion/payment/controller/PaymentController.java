package com.xelvo.companion.payment.controller;

import com.xelvo.companion.common.api.ApiResponse;
import com.xelvo.companion.common.api.PageResponse;
import com.xelvo.companion.payment.dto.*;
import com.xelvo.companion.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/mine")
    public ApiResponse<PageResponse<PaymentResponse>> mine(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.success(paymentService.mine(pageable));
    }

    @GetMapping("/booking/{bookingId}")
    public ApiResponse<PaymentResponse> get(@PathVariable Long bookingId) {
        return ApiResponse.success(paymentService.getByBooking(bookingId));
    }

    @PostMapping("/booking/{bookingId}/order")
    public ApiResponse<PaymentResponse> createOrder(@PathVariable Long bookingId) {
        return ApiResponse.success("Payment order created", paymentService.createProviderOrder(bookingId));
    }

    @PostMapping("/booking/{bookingId}/confirm-mock")
    public ApiResponse<PaymentResponse> confirmMock(
            @PathVariable Long bookingId,
            @Valid @RequestBody MockPaymentRequest request) {
        return ApiResponse.success("Payment confirmed", paymentService.confirmMockPayment(bookingId, request));
    }
}
