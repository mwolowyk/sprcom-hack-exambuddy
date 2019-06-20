# sprcom-hack-exambuddy

This repository contains a draft project for semantic analysis of PDF documents and building semantic matching between PDF documents.

To start the application: 
1. `docker-compose up -d`
2. run ExambuddyApplication.java
3. Go to http://localhost:8080/swagger-ui.html

The application contains two endpoints:
1. search semantic entities from text
2. search semantic entities from PDF file

After calling an endpoint the PDF text extractor extracts the text from a PDF page
and sends it to DBpedia Spotlight API: https://www.dbpedia-spotlight.org/demo/. 

DBpedia Spotlight API returns list of Semantic Entities mapping to DBpedia Resources 
(Like this: http://dbpedia.org/page/Sustainable_Development_Goals)

The application has installation and configuration for Neo4j Graph database to store the entities
