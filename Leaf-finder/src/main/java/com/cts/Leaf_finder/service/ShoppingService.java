package com.cts.Leaf_finder.service;

import com.cts.Leaf_finder.model.Category;
import com.cts.Leaf_finder.model.Product;
import com.cts.Leaf_finder.model.ProductVariation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ShoppingService {

    public List<String> getLeafNodes(Category category){
        return Optional.ofNullable(category)
                .map(Category::getProducts)
                .map(products -> products.stream()
                        .flatMap(product -> getLeafNodesForProduct(product).stream())
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }

    private List<String> getLeafNodesForProduct(Product product) {
        return Optional.ofNullable(product)
                .map(Product::getVariations)
                .map(variations-> variations.stream()
                        .map(variation-> getLeafNodeInfo(variation))
                        .collect(Collectors.toList()))
                .orElse((List.of()));
    }

    private String getLeafNodeInfo(ProductVariation variation) {
        return Optional.ofNullable(variation)
                .map(variant-> "Product Variation ID:" +variant.getId()+
                        ", Name: "+variant.getVariationName()+
                        ", Price: "+variant.getPrice()+
                        ", Stock: "+variant.getStock())
                .orElse("No variant info available");
    }


}
