<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Upload Excel File</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            color: #333;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .container {
            background: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(110, 117, 107, 1);
            width: 100%;
            max-width: 500px;
        }

        h2 {
            margin-top: 0;
            color: #4a90e2;
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
        }

        input[type="text"],
        input[type="file"] {
            width: calc(100% - 22px);
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
            margin-bottom: 16px;
        }

        .radio-group {
            margin-bottom: 16px;
        }

        .radio-group label {
            display: inline-block;
            margin-right: 20px;
            font-weight: normal;
        }

        input[type="radio"] {
            margin-right: 8px;
        }

        input[type="submit"] {
            background-color: #4a90e2;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }

        input[type="submit"]:hover {
            background-color: #357abd;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Upload Excel File</h2>
        <form action="upload" method="post" enctype="multipart/form-data">
            <label for="tableName">Table Name:</label>
            <input type="text" id="tableName" name="tableName" required>
            <br>
            <label for="file">Choose Excel File:</label>
            <input type="file" id="file" name="file" accept=".xls,.xlsx" required>
            <br>
            <div class="radio-group">
                <input type="radio" id="insert" name="action" value="insert" checked>
                <label for="insert">Insert</label>
                <input type="radio" id="update" name="action" value="update">
                <label for="update">Update</label>
            </div>
            <br>
            <input type="submit" value="Upload">
        </form>
    </div>
</body>
</html>