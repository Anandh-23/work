package com.cts;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/upload")
@MultipartConfig(
    fileSizeThreshold = 512000, // 500KB
    maxFileSize = 10485760,     // 10MB
    maxRequestSize = 20971520   // 20MB
)
public class FileUploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String tableName = request.getParameter("tableName");
        
        if (tableName == null || tableName.trim().isEmpty() || action == null || action.trim().isEmpty()) {
            request.setAttribute("error", "Table name or action is missing");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }
        
        Part filePart = request.getPart("file");
        if (filePart == null || filePart.getSize() == 0) {
            request.setAttribute("error", "No file uploaded");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }

        try (InputStream inputStream = filePart.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            
            if (!rowIterator.hasNext()) {
                request.setAttribute("error", "Empty Excel file");
                request.getRequestDispatcher("index.jsp").forward(request, response);
                return;
            }
            
            Row headerRow = rowIterator.next(); // Read the header row
            List<String> columns = new ArrayList<>();
            for (Cell cell : headerRow) {
                columns.add(cell.getStringCellValue());
            }
            
            StringBuilder result = new StringBuilder();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                StringBuilder query = new StringBuilder();
                
                if (action.equals("insert")) {
                    query.append("INSERT INTO ").append(tableName).append(" (");
                    StringBuilder values = new StringBuilder("VALUES (");
                    
                    for (int i = 1; i < row.getLastCellNum(); i++) { // Skip the first column which is ID
                        Cell cell = row.getCell(i);
                        if (cell != null) {
                            query.append(columns.get(i)).append(",");
                            if (cell.getCellType() == CellType.STRING) {
                                values.append("'").append(cell.getStringCellValue()).append("',");
                            } else if (cell.getCellType() == CellType.NUMERIC) {
                                values.append(cell.getNumericCellValue()).append(",");
                            }
                        }
                    }
                    
                    query.deleteCharAt(query.length() - 1); // Remove trailing comma
                    values.deleteCharAt(values.length() - 1); // Remove trailing comma
                    
                    query.append(") ").append(values).append(");");
                    result.append(query).append("\n");
                    
                } else if (action.equals("update")) {
                    String id = row.getCell(0).toString();
                    query.append("UPDATE ").append(tableName).append(" SET ");
                    
                    for (int i = 1; i < row.getLastCellNum(); i++) { // Skip the first column which is ID
                        Cell cell = row.getCell(i);
                        if (cell != null) {
                            query.append(columns.get(i)).append(" = ");
                            if (cell.getCellType() == CellType.STRING) {
                                query.append("'").append(cell.getStringCellValue()).append("', ");
                            } else if (cell.getCellType() == CellType.NUMERIC) {
                                query.append(cell.getNumericCellValue()).append(", ");
                            }
                        }
                    }
                    
                    query.setLength(query.length() - 2); // Remove trailing comma and space
                    
                    query.append(" WHERE id = ").append(id).append(";");
                    result.append(query).append("\n");
                }
            }

            if (result.length() == 0) {
                request.setAttribute("error", "No data found in the Excel file");
            } else {
                request.setAttribute("result", result.toString());
            }
            request.getRequestDispatcher("result.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "Error processing the Excel file: " + e.getMessage());
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
}