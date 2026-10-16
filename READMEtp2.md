# TP2 DevOps Correction Docker

### 2.1 What are testcontainers ?

Testcontainers is a Java library that allows us to create and run temporary Docker containers during automated tests. 
In our case, we use it to start a PostgreSQL database so that our integration tests can interact with a real database 
instead of a simulation. The container is automatically started when the tests run and removed afterward. 
This allows us to test our application in an isolated and reproducible environment, without having to 
manually install or configure a database.

### 2.2 For what purpose do we need to use secured variables ?

We need secured variables because our pipeline requires sensitive information, such as Docker Hub credentials, 
to connect to external services. If we put these directly in our workflow file, anyone with access to the repository 
could read them and potentially use our account. GitHub Secrets allows us to store these credentials securely and access
them in our pipeline using ${{ secrets.DOCKERHUB_TOKEN }} for example. This way, we can authenticate without exposing 
passwords or tokens in our source code.

### 2-3 Why did we put needs: build-and-test-backend on this job?

By default, GitHub Actions runs jobs in parallel. The needs: test-backend instruction creates a dependency between the 
two jobs, meaning that the Docker image build job will only start once the backend tests have passed. 
If the tests fail, the build job is skipped. Without this dependency, Docker could build and publish an image 
containing broken code, even if the tests are failing. This ensures that we only publish images after verifying that 
our application works correctly.

### 2-4 For what purpose do we need to push docker images?

Pushing Docker images to Docker Hub allows other machines to download and run our application without rebuilding it. 
It also makes deployment easier and allows us to manage different versions using tags.