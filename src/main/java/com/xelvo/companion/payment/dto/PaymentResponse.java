package com.xelvo.companion.payment.dto;

import java.math.BigDecimal;

public record PaymentResponse(
        Long id,
        Long bookingId,
        BigDecimal amount,
        String providerOrderId,
        String providerPaymentId,
        String status
) {}
