package com.xelvo.companion.publicapi;

import com.xelvo.companion.common.api.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public")
public class PublicController {

    @GetMapping("/cities")
    public ApiResponse<List<Map<String, Object>>> cities() {
        return ApiResponse.success(List.of(
                Map.of("name", "Delhi", "companions", 500),
                Map.of("name", "Mumbai", "companions", 450),
                Map.of("name", "Bengaluru", "companions", 400),
                Map.of("name", "Goa", "companions", 300),
                Map.of("name", "Pune", "companions", 250),
                Map.of("name", "Hyderabad", "companions", 200)
        ));
    }

    @GetMapping("/categories")
    public ApiResponse<List<String>> categories() {
        return ApiResponse.success(List.of(
                "Coffee", "Food", "Travel", "Music", "Art", "Sports",
                "Movies", "Gaming", "Photography", "City walks", "Events"
        ));
    }
}
