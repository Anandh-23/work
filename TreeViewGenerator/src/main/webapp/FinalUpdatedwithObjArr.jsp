<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>JSON to Tree View with Search</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        textarea {
            width: 100%;
            height: 150px;
            font-family: monospace;
            margin-bottom: 10px;
        }
        button {
            padding: 10px 20px;
            font-size: 16px;
            cursor: pointer;
            margin-right: 10px;
        }
        input[type="text"] {
            padding: 8px;
            width: 300px;
            font-size: 16px;
            margin-right: 10px;
        }
        ul, #treeView {
            list-style-type: none;
            padding-left: 20px;
        }
        li {
            margin-left: 20px;
            line-height: 1.6;
        }
        .object-caret, .array-caret, .index-caret {
            cursor: pointer;
            user-select: none;
        }
        .object-caret::before {
            content: "+ { }";
            color: black;
            display: inline-block;
            margin-right: 6px;
        }
        .object-caret-down::before {
            margin-left: -5px;
            content: "- { } ";
        }
        .array-caret::before {
            content: "+ [ ]";
            color: black;
            display: inline-block;
            margin-right: 6px;
        }
        .array-caret-down::before {
            content: "- [ ] ";
        }
        .index-caret::before {
            content: "+ { }";
            color: black;
            display: inline-block;
            margin-right: 6px;
        }
        .index-caret-down::before {
            content: "- { } ";
        }
        .nested {
            display: none;
        }
        .active {
            display: block;
        }
        .highlight {
            background-color: yellow;
            font-weight: bold;
        }
        .array-item {
            display: flex;
            align-items: center;
        }
        .index {
            display: inline-block;
            width: 20px;
            text-align: center;
            margin-right: 10px;
            color: black;
        }
        .value-symbol {
            display: inline-block;
            margin-left: 12.5px;
            width: 5px;
            height: 5px;
            background-color: blue;
            margin-right: 6px;
            vertical-align: middle;
        }
    </style>
</head>
<body>
 
    <h2>JSON to Tree View Converter with Search</h2>
    <textarea id="jsonInput" placeholder="Enter your JSON here..."></textarea>
    <button onclick="convertToTreeView()">Convert to Tree View</button>
    <input type="text" id="searchInput" placeholder="Search...">
    <button onclick="searchTree()">Search</button>
   
    <div id="treeView"></div>
 
    <script>
        function buildTree(obj, parent) {
            for (const key in obj) {
                if (obj.hasOwnProperty(key)) {
                    const li = document.createElement('li');
 
                    if (typeof obj[key] === 'object' && obj[key] !== null && !Array.isArray(obj[key])) {
                        li.innerHTML = `<span class="object-caret">${key}</span>`;
                        const ul = document.createElement('ul');
                        ul.classList.add('nested');
                        buildTree(obj[key], ul);
                        li.appendChild(ul);
 
                    } else if (Array.isArray(obj[key])) {
                        li.innerHTML = `<span class="array-caret">${key}</span>`;
                        const ul = document.createElement('ul');
                        ul.classList.add('nested');
                        obj[key].forEach((item, index) => {
                            const arrayItem = document.createElement('li');
                            if (typeof item === 'object' && item !== null) {
                                arrayItem.innerHTML = `<span class="index-caret">${index}</span>`;
                                const subUl = document.createElement('ul');
                                subUl.classList.add('nested');
                                buildTree(item, subUl);
                                arrayItem.appendChild(subUl);
                            } else {
                                arrayItem.classList.add('array-item');
                                const indexSpan = document.createElement('span');
                                indexSpan.className = 'index';
                                indexSpan.textContent = `[${index}]`;
                                arrayItem.appendChild(indexSpan);
                                if (item !== undefined && item !== null) {
                                    arrayItem.innerHTML += `<span class="value-symbol"></span>${item}`;
                                } else {
                                    arrayItem.innerHTML += `<span class="value-symbol"></span>[null]`;
                                }
                            }
                            ul.appendChild(arrayItem);
                        });
                        li.appendChild(ul);
 
                    } else if (obj[key] !== undefined && obj[key] !== null) {
                        li.innerHTML = `<span class="value-symbol"></span>${key}: ${obj[key]}`;
                    } else {
                        li.innerHTML = `<span class="value-symbol"></span>${key}: [null]`;
                    }
 
                    parent.appendChild(li);
                }
            }
        }
 
        function convertToTreeView() {
            const jsonInput = document.getElementById('jsonInput').value;
            const treeView = document.getElementById('treeView');
            treeView.innerHTML = '';  // Clear the previous tree view
 
            try {
                const jsonObject = JSON.parse(jsonInput);
                const ul = document.createElement('ul');
                buildTree(jsonObject, ul);
                treeView.appendChild(ul);
                addToggleFunctionality();
            } catch (e) {
                alert('Invalid JSON. Please enter a valid JSON object.');
            }
        }
 
        function addToggleFunctionality() {
            const objectTogglers = document.getElementsByClassName('object-caret');
            for (let i = 0; i < objectTogglers.length; i++) {
                objectTogglers[i].addEventListener('click', function () {
                    const nested = this.parentElement.querySelector('.nested');
                    if (nested) {
                        nested.classList.toggle('active');
                        this.classList.toggle('object-caret');
                        this.classList.toggle('object-caret-down');
                    }
                });
            }
 
            const arrayTogglers = document.getElementsByClassName('array-caret');
            for (let i = 0; i < arrayTogglers.length; i++) {
                arrayTogglers[i].addEventListener('click', function () {
                    const nested = this.parentElement.querySelector('.nested');
                    if (nested) {
                        nested.classList.toggle('active');
                        this.classList.toggle('array-caret');
                        this.classList.toggle('array-caret-down');
                    }
                });
            }
 
            const indexTogglers = document.getElementsByClassName('index-caret');
            for (let i = 0; i < indexTogglers.length; i++) {
                indexTogglers[i].addEventListener('click', function () {
                    const nested = this.parentElement.querySelector('.nested');
                    if (nested) {
                        nested.classList.toggle('active');
                        this.classList.toggle('index-caret');
                        this.classList.toggle('index-caret-down');
                    }
                });
            }
        }
 
        function searchTree() {
            const searchValue = document.getElementById('searchInput').value.toLowerCase();
            const searchRegExp = new RegExp(`(${searchValue})`, 'gi'); // Regex to match search term case-insensitively
            const treeView = document.getElementById('treeView');
 
            // Clear previous highlights
            removePreviousHighlights(treeView);
 
            // Highlight current search terms and expand ancestors
            const lis = treeView.querySelectorAll('li');
            lis.forEach(li => {
                if (li.textContent.toLowerCase().includes(searchValue)) {
                    highlightText(li, searchRegExp);
                    expandAncestors(li);
                }
            });
        }
 
        function removePreviousHighlights(element) {
            const highlightSpans = element.querySelectorAll('.highlight');
            highlightSpans.forEach(span => {
                const parent = span.parentNode;
                parent.replaceChild(document.createTextNode(span.textContent), span);
            });
        }
 
        function highlightText(element, regExp) {
            const childNodes = Array.from(element.childNodes);
 
            childNodes.forEach(node => {
                if (node.nodeType === Node.TEXT_NODE) {
                    const newHTML = node.textContent.replace(regExp, '<span class="highlight">$1</span>');
                    if (newHTML !== node.textContent) {
                        const span = document.createElement('span');
                        span.innerHTML = newHTML;
                        element.replaceChild(span, node);
                    }
                } else if (node.nodeType === Node.ELEMENT_NODE) {
                    highlightText(node, regExp);
                }
            });
        }
 
        function expandAncestors(li) {
            let parent = li.parentElement;
			while (parent && parent.id !== 'treeView') {
                if (parent.tagName === 'UL') {
                    parent.classList.add('active');
                }
                if (parent.previousElementSibling) {
                    const previousSibling = parent.previousElementSibling;
                    if (previousSibling.classList.contains('object-caret')) {
                        previousSibling.classList.add('object-caret-down');
                        previousSibling.classList.remove('object-caret');
                    } else if (previousSibling.classList.contains('array-caret')) {
                        previousSibling.classList.add('array-caret-down');
                        previousSibling.classList.remove('array-caret');
                    } else if (previousSibling.classList.contains('index-caret')) {
                        previousSibling.classList.add('index-caret-down');
                        previousSibling.classList.remove('index-caret');
                    }
                }
                parent = parent.parentElement;
            }
        }
    </script>
 
</body>
</html>