# ADSO Chat Application

## Overview

ADSO Chat is a containerized application that leverages Docker Compose to manage three main services: a backend server, an Ollama language processing service, and a frontend user interface. This application is designed to provide an interactive chat experience with advanced language processing capabilities.

## Services

1. **ADSO Chat Backend**: Handles server-side logic and API requests.
2. **ADSO Chat Ollama**: Provides language model processing for enhanced chat functionality.
3. **ADSO UI**: Delivers the user interface for interacting with the chat application.

## Prerequisites

- Docker
- Docker Compose

## Quick Start

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd <repository-directory>

2. Build and start the services:
   ```bash
   docker-compose up --build -d

3. Access the application:

* Backend: http://localhost:8080
* Ollama: http://localhost:11434
* UI: http://localhost

4. Usage
* View Logs
  ```bash
  `docker-compose logs -f`
* Stop Services
  ```bash
  `docker-compose down`
* Rebuild and Restart Services
   ```bash
   `docker-compose up -d --build`


5. Configuration
The application uses a custom bridge network named mynetwork for inter-service communication. Each service is configured with specific options for optimal performance and reliability.
Data Persistence
The Ollama service uses a bind mount to persist data:

Local ./data directory is mounted to /data in the Ollama container.


## About Ollama

### What is Ollama?
Ollama is a service used to handle language models and facilitate advanced language processing tasks. It can be integrated into various applications to provide functionalities such as language translation, text generation, and more.

### Usage in ADSO Chat
In the context of the ADSO Chat application, Ollama is used to process and generate responses based on the inputs it receives. This enhances the chatbot's ability to understand and generate natural language.

### Configuration
Ensure that the Ollama endpoint is correctly configured in your application settings. This typically involves specifying the URL or IP address where the Ollama service is running.

### Data Volume
The configuration includes a bind mount for the `./data` directory to `/data` within the Ollama container. This is used to persist data across container restarts and can be useful for storing model files or other relevant data.

### Network Configuration
Ollama is connected to the same custom bridge network (`mynetwork`) as the other services, allowing it to communicate seamlessly with the backend and UI components of the ADSO Chat application.