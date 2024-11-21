package com.cts.Leaf_finder.controller;

import com.cts.Leaf_finder.model.Category;
import com.cts.Leaf_finder.service.ShoppingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ShoppingController {

    @Autowired
    private ShoppingService shoppingService;

    @PostMapping("/processCategory")
    public List<String> processCategoryData(@RequestBody Category category){
        return shoppingService.getLeafNodes(category);
    }
}
