package com.onsuyum.restaurant.controller;

import com.onsuyum.common.StatusEnum;
import com.onsuyum.common.response.SuccessResponseBody;
import com.onsuyum.restaurant.domain.service.RestaurantCategoryService;
import com.onsuyum.restaurant.dto.response.CategoryResponse;
import com.onsuyum.restaurant.dto.response.RestaurantResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RestaurantCategoryController {

    private final RestaurantCategoryService restaurantCategoryService;

    @PostMapping("/restaurants/{id}/categories")
    public ResponseEntity<SuccessResponseBody<Map<String, Object>>> saveAllRestaurantCategoryById(@PathVariable Long id, @RequestBody Set<String> categoryNames) {
        if (categoryNames.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "카테고리를 한개 이상 요청 보내야 합니다.");
        }

        Map<String, Object> responses = restaurantCategoryService.saveAllRestaurantCategoryWithRequest(id, categoryNames, true);

        return SuccessResponseBody
                .toResponseEntity(
                        StatusEnum.SUCCESS_CREATE_CATEGORIES_BY_RESTAURANT,
                        responses
                );
    }

    @GetMapping("/restaurants/{id}/categories")
    public ResponseEntity<SuccessResponseBody<Page<CategoryResponse>>> findAllCategoryByRestaurantId(@PathVariable Long id, Pageable pageable) {
        Page<CategoryResponse> categoryResponsePage = restaurantCategoryService.findAllCategoryByRestaurantIdWithRequest(pageable, id, true);

        if (categoryResponsePage.isEmpty()) {
            return SuccessResponseBody.toEmptyResponseEntity(StatusEnum.NO_CONTENT_RESTAURANTS);
        }

        return SuccessResponseBody
                .toResponseEntity(
                        StatusEnum.SUCCESS_GET_RESTAURANTS,
                        categoryResponsePage
                );
    }

    @GetMapping("/categories/{id}/restaurants")
    public ResponseEntity<SuccessResponseBody<Page<RestaurantResponse>>> findAllRestaurantByCategoryId(@PathVariable Long id, Pageable pageable) {
        Page<RestaurantResponse> restaurantResponsePage = restaurantCategoryService.findAllRestaurantByCategoryIdWithRequest(pageable, id);

        if (restaurantResponsePage.isEmpty()) {
            return SuccessResponseBody.toEmptyResponseEntity(StatusEnum.NO_CONTENT_CATEGORIES);
        }

        return SuccessResponseBody
                .toResponseEntity(
                        StatusEnum.SUCCESS_GET_CATEGORIES,
                        restaurantResponsePage
                );
    }
}
