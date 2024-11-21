package com.cts.Leaf_finder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    private Long id;
    private String productName;
    private List<ProductVariation> variations;
}
