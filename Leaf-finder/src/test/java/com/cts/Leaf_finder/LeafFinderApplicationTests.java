package com.cts.Leaf_finder.ServiceTest;

import com.cts.Leaf_finder.model.Category;
import com.cts.Leaf_finder.model.Product;
import com.cts.Leaf_finder.model.ProductVariation;
import com.cts.Leaf_finder.service.ShoppingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ShoppingServiceTest {

     private ShoppingService shoppingService;
     private Category category;
     private Product product;
     private ProductVariation productVariation;

 @BeforeEach
 public void setUp() {
     shoppingService = new ShoppingService();

     category = mock(Category.class);
     product = mock(Product.class);
     productVariation = mock(ProductVariation.class);
 }

 @Test
 public void testGetLeafNodes_whenCategoryIsNull() {
     List<String> leafNodes = shoppingService.getLeafNodes(null);
     assertEquals(0, leafNodes.size(), "Leaf nodes should be empty for null category");
 }

 @Test
 public void testGetLeafNodes_whenNoProductsInCategory() {
     when(category.getProducts()).thenReturn(List.of());

     List<String> leafNodes = shoppingService.getLeafNodes(category);
     assertEquals(0, leafNodes.size(), "Leaf nodes should be empty when category has no products");
 }

 @Test
 public void testGetLeafNodes_whenCategoryHasProductsAndVariations() {
     ProductVariation variation1 = mock(ProductVariation.class);
     when(variation1.getId()).thenReturn(1L);
     when(variation1.getVariationName()).thenReturn("Color");
     when(variation1.getPrice()).thenReturn(100.0);
     when(variation1.getStock()).thenReturn(10);
     ProductVariation variation2 = mock(ProductVariation.class);
     when(variation2.getId()).thenReturn(2L);
     when(variation2.getVariationName()).thenReturn("Size");
     when(variation2.getPrice()).thenReturn(150.0);
     when(variation2.getStock()).thenReturn(5);

     when(product.getVariations()).thenReturn(List.of(variation1, variation2));
     when(category.getProducts()).thenReturn(List.of(product));

     List<String> leafNodes = shoppingService.getLeafNodes(category);

     assertEquals(2, leafNodes.size(), "There should be two leaf nodes");
     assertEquals("Product Variation ID:1, Name: Color, Price: 100.0, Stock: 10", leafNodes.get(0));
     assertEquals("Product Variation ID:2, Name: Size, Price: 150.0, Stock: 5", leafNodes.get(1));
 }

 @Test
 public void testGetLeafNodeInfo_whenProductVariationIsNull() {
     String result = shoppingService.getLeafNodeInfo(null);
     assertEquals("No variant info available", result, "Expected default variant info message for null variation");
 }

 @Test
 public void testGetLeafNodeInfo_whenProductVariationIsNotNull() {
      when(productVariation.getId()).thenReturn(1L);
      when(productVariation.getVariationName()).thenReturn("Color");
      when(productVariation.getPrice()).thenReturn(100.0);
      when(productVariation.getStock()).thenReturn(10);
      String result = shoppingService.getLeafNodeInfo(productVariation);
      assertEquals("Product Variation ID:1, Name: Color, Price: 100.0, Stock: 10", result);
      }

}
