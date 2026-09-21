package com.xelvo.companion.companion.dto;

import jakarta.validation.constraints.Size;
import java.util.List;

public record UpdateInterestsRequest(
        @Size(max = 20) List<@Size(max = 60) String> interests
) {}
