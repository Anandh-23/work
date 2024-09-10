<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>SQL Query Result</title>
    <style>
        .container {
            width: 80%;
            margin: 0 auto;
        }

        .textarea {
            width: 100%;
            height: 200px;
            margin-top: 10px;
            box-sizing: border-box;
        }

        .copy-button {
            margin-top: 10px;
            padding: 10px;
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
        }

        .copy-button:hover {
            background-color: #45a049;
        }

        /* Modal styles */
        .modal {
            display: none; 
            position: fixed; 
            z-index: 1; 
            left: 0;
            top: 0;
            width: 100%; 
            height: 100%; 
            overflow: auto; 
            background-color: rgb(0,0,0); 
            background-color: rgba(0,0,0,0.4); 
        }

        .modal-content {
            background-color: #fefefe;
            margin: 15% auto;
            padding: 20px;
            border: 1px solid #888;
            width: 80%; 
            max-width: 500px; 
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }

        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
        }

        .close:hover,
        .close:focus {
            color: black;
            text-decoration: none;
            cursor: pointer;
        }
    </style>
    <script>
        function copyToClipboard() {
            var textArea = document.getElementById("queryOutput");
            textArea.select();
            document.execCommand("copy");

            var modal = document.getElementById("modal");
            modal.style.display = "block";

            setTimeout(function() {
                closeModal();
            }, 3000); 
        }

        function closeModal() {
            document.getElementById("modal").style.display = "none";
        }
    </script>
</head>
<body>
<div class="container">
    <h2>Generated SQL Query</h2>

    <%
        String result = (String) request.getAttribute("result");
        String error = (String) request.getAttribute("error");

        if (result != null) {
    %>
        <textarea id="queryOutput" class="textarea" readonly><%= result %></textarea>
        <button class="copy-button" onclick="copyToClipboard()">Copy to Clipboard</button>
    <%
        } else if (error != null) {
    %>
        <p style="color: red;"><%= error %></p>
    <%
        }
    %>
    
    <br>
    <a href="index.jsp">Go back</a>
</div>

<div id="modal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal()">&times;</span>
        <p>Query Copied!</p>
    </div>
</div>
</body>
</html>
