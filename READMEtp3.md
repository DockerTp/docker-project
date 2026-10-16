# TP3 Ansible

### 3.1

The inventory file defines the remote server, the SSH user, and the private key used to connect to it. 
Our server belongs to the prod group.

We can then use several commands:
- ansible all -i inventories/setup.yml -m ping: Tests the SSH connection to our server.
- ansible all -i inventories/setup.yml -m setup -a "filter=ansible_distribution*": Retrieves information about the server's operating system using Ansible facts.
- ansible all -i inventories/setup.yml -m apt -a "name=apache2 state=absent" --become: Removes Apache2 from the server using administrator privileges.

### 3.2

Our main playbook is divided into two parts. The first uses the docker role to install Docker, 
its dependencies, and ensure the service is running.

The second uses the Python virtual environment containing the Docker SDK and executes the network, database, backend, 
and http_server roles to deploy our application.

Both use become: true to execute tasks with administrator privileges.

### 3.3

We use the docker_container module to deploy our database, backend, and HTTPD proxy using images from Docker Hub.

The database uses a persistent volume (db-data) and connects to back-network. The backend connects to both networks 
and uses environment variables to access the database. The HTTPD proxy connects to front-network and exposes port 80.

All containers use restart_policy: unless-stopped to restart automatically 
and pull: always to check for and pull updated images.

### 3.4 Is it really safe to deploy automatically every new image on the hub ? explain. What can I do to make it more secure?

Automatically deploying every new image from Docker Hub is risky because an image could contain bugs, vulnerabilities, 
or malicious code.

To make it safer, we could deploy it after the CI pipeline has fully passed, and/or require a manual approval.