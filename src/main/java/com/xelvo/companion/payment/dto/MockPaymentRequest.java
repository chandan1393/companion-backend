package com.xelvo.companion.payment.dto;

import jakarta.validation.constraints.NotBlank;

public record MockPaymentRequest(
        @NotBlank String providerPaymentId
) {}
