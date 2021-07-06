When implementing RestDocs, remember to use pupperpals as a model for:

- When creating your project, be sure to include Spring Rest DOcs from the testing dependencies section
- Modify your build.gradle file to match the one in this project. Relevant methods are:
-- plugins
   ext
   test
   asciidoctor
   asciidoctor.doLast
   bootJar
  
- Create the folder /docs/asciidoc under src and create the file index.adoc inside it
  This file determines what is actually included in your documentation
  Be sure to install the adoc plugin if prompted
  
- Finish *one* api integration test for each api with .andDo(document())