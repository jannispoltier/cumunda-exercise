# Process Execution

To successfully execute the process, multiple steps are required.

1. Create a project in the Camunda web version.
2. Add the model located in the process-model directory to the project
3. [Create a cluster](https://docs.camunda.io/docs/components/console/manage-clusters/create-cluster/) for the project 
4. Download the code in the manufacture-supplements directory or clone the repository
5. [Create an API key](https://docs.camunda.io/docs/components/console/manage-clusters/manage-api-clients/) for the cluster
6. Copy the API credentials for the Spring application and them to the application.properties file
5. In your terminal, navigate to this directory
6. Install the dependencies with ```mvn clean install```
7. Run the application with ```mvn spring-boot:run```

Now the application should be running and be connected to the cluster. In the web version, you can now run the process. While you need to manually execute user tasks in the Camunda Tasklist, all other tasks and message flows should be automatically run.
