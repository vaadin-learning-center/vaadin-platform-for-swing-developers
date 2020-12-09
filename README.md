# Java EE example with Swing and Vaadin UIs

This is a simple example application that has (Remote) EJB for Customer entities, and simple CRUD user interfaces implemented by both Swing based desktop application and Vaadin based web application. With the UI code examples, you'll see similarities with the programming model and can consider based on the example, what it might mean to convert your legacy desktop app to Vaadin based Web application.

### The example contains following modules:

 * *domain* - (JPA) entities. JPA entities are used as DTOs as well.
 * *ejb-ap* - the API for the (Remote) EJB via updates to "customer database" are done
 * *server* - WAR project that contains both the EJB implementation and Vaadin UI. This could naturally be split to two modules (and EAR) as well.
 * *desktop* - A simple Swing based client application that uses the same EJB as the Vaadin UI

### To play with the demo:

 * Use Java 8 to run the demo
 * build from top level with **mvn install**
 * import the project to your IDE and make one top level build
 * start the server module with **mvn tomee:run** from the module root or by deploying the server module manually to TomEE server configured in you IDE.
 * Web UI is then available at **http://localhost:8080/server-1.0-SNAPSHOT/**
 * The Swing UI can be launched directly from IDE by executing main method from SwingApplication class in desktop module. **Note: the server needs to be running**.

### Credits

This is a fork of https://github.com/mstahv/ejb-swing-vaadin-crud and the example was originally provided by Matti Tahvonen as an example migration from Swing to Vaadin 7 using more advanced concepts