package com.example.servlets;
 
import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
 
@WebServlet(name = "DisplayTreeServlet", urlPatterns = {"/DisplayTreeServlet"})
public class DisplayTreeServlet extends HttpServlet {
 
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jsonInput = request.getParameter("jsonInput");
 
        JsonElement jsonElement = JsonParser.parseString(jsonInput);
        String jsonTree = convertJsonToTree(jsonElement);
 
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(jsonTree);
        out.flush();
    }
 
    private String convertJsonToTree(JsonElement jsonElement) {
        Gson gson = new Gson();
        if (jsonElement.isJsonObject()) {
            return gson.toJson(createTreeNode("root", jsonElement.getAsJsonObject()));
        } else if (jsonElement.isJsonArray()) {
            return gson.toJson(createTreeNode("root", jsonElement.getAsJsonArray()));
        } else {
            return gson.toJson(createTreeNode("root", jsonElement));
        }
    }
 
    private JsonObject createTreeNode(String name, JsonElement element) {
        JsonObject node = new JsonObject();
        node.addProperty("text", name);
 
        if (element.isJsonObject()) {
            node.addProperty("icon", "jstree-folder");
            node.add("children", convertJsonObjectToTree(element.getAsJsonObject()));
        } else if (element.isJsonArray()) {
            node.addProperty("icon", "jstree-folder");
            node.add("children", convertJsonArrayToTree(element.getAsJsonArray()));
        } else {
            node.addProperty("icon", "jstree-file");
            node.addProperty("text", name + ": " + element.getAsString());
        }
 
        return node;
    }
 
    private JsonArray convertJsonObjectToTree(JsonObject jsonObject) {
        JsonArray children = new JsonArray();
        for (var entry : jsonObject.entrySet()) {
            children.add(createTreeNode(entry.getKey(), entry.getValue()));
        }
        return children;
    }
 
    private JsonArray convertJsonArrayToTree(JsonArray jsonArray) {
        JsonArray children = new JsonArray();
        for (JsonElement element : jsonArray) {
            children.add(createTreeNode("", element));
        }
        return children;
    }
}