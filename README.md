# WebMVCEmployees 🚀

A modern Java Spring Boot + Neo4j **RESTful Microservice** that manages employee data, supports advanced querying, and models organizational hierarchy.  
Built as part of a Cloud Computing course assignment.

---

## 📚 Features

✅ Create and retrieve employees securely  
✅ Built-in validation for email, password, roles, and birthdates  
✅ Pagination support for search endpoints

### 🔍 Advanced filtering:
- By **email domain**
- By **role**
- By **age** (in years)

### 🧑‍💼 Organizational hierarchy:
- Assign a manager to an employee
- Get an employee’s manager
- Get manager’s subordinates
- Remove a manager from an employee

### 🔒 Security:
- Passwords are **hidden** in all API responses
- Prevents creating weak passwords  

---

## 🛠️ Getting Started

### ⚙️ Prerequisites
- Java 21+
- Gradle 8+
- Docker (for running Neo4j)

### 🐳 Start Neo4j with Docker Compose

Make sure this file exists in your project root and is named `compose.yaml`:

```yaml
services:
  neo4j:
    image: 'neo4j:latest'
    environment:
      - NEO4J_AUTH=neo4j/notverysecret
    ports:
      - '7687:7687'
      - '7474:7474'
```
## 🚀 Run the Project

``` 
git clone https://github.com/yazanateer/WebMVCEmployees.git
cd WebMVCEmployees
./gradlew bootRun