package com.cts.Leaf_finder.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductVariation {
    private Long id;
    private String variationName;
    private double price;
    private int stock;
}
