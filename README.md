# GitHub Access Report Service

## Problem Statement

Organizations often need visibility into who has access to which repositories in GitHub.
This project provides a service that connects to GitHub, retrieves repository and user access data, and generates a structured access report for a given organization or user.

---

## Features

* Authenticates securely with GitHub using a Personal Access Token
* Retrieves repositories for a given organization or user
* Fetches collaborators for each repository
* Determines access levels (ADMIN / WRITE / READ)
* Aggregates data to map users → repositories
* Exposes a REST API endpoint to fetch the report in JSON format

---

## API Endpoint

### Get Access Report

```
GET /api/github/access-report?org={orgName}
```

### Example

```
http://localhost:8080/api/github/access-report?org=tejuingalagi
```

### Sample Response

```json
[
  {
    "username": "Anusha03801",
    "repositories": [
      {
        "repository": "finance-dashboard-backend",
        "access": "WRITE"
      }
    ]
  },
  {
    "username": "tejuingalagi",
    "repositories": [
      {
        "repository": "github-access-report",
        "access": "ADMIN"
      }
    ]
  }
]
```

---

## How to Run the Project

### 1. Clone the repository

```
git clone https://github.com/your-username/github-access-report.git
cd github-access-report
```

### 2. Configure GitHub Token

Update `application.yaml`:

```yaml
github:
  base-url: https://api.github.com
  token: YOUR_GITHUB_PERSONAL_ACCESS_TOKEN
```

👉 Generate token from GitHub → Settings → Developer Settings → Personal Access Tokens

---

### 3. Run the application

Using Maven:

```
mvn spring-boot:run
```

Or run the main class in IDE.

---

### 4. Test the API

Open browser or Postman:

```
http://localhost:8080/api/github/access-report?org=your-org
```

---

## Authentication

* Uses GitHub Personal Access Token (PAT)
* Token is passed via Authorization header:

```
Authorization: Bearer <token>
```

* Ensures secure communication with GitHub APIs

---

## Design & Implementation Details

### 1. API Integration

* `/orgs/{org}/repos` → fetch organization repositories
* Fallback: `/users/{user}/repos` → for user accounts
* `/repos/{org}/{repo}/collaborators` → fetch access users

---

### 2. Access Level Mapping

Access is derived from GitHub permissions:

* `admin = true` → ADMIN
* `push = true` → WRITE
* `pull = true` → READ

---

### 3. Aggregation Logic

* Data is transformed into:

  ```
  User → List of Repositories + Access
  ```
* Uses `ConcurrentHashMap` for thread-safe operations

---

### 4. Performance (Scale Handling)

* Uses `parallelStream()` for processing repositories concurrently
* Avoids sequential API calls
* Designed to handle:

  * 100+ repositories
  * 1000+ users

---

### 5. Error Handling

* Gracefully handles API failures
* Logs errors without breaking application flow
* Returns empty response if data cannot be fetched

---

## Assumptions & Limitations

* GitHub API restricts collaborator data for public organizations
* Collaborator details are only available if:

  * The authenticated user has access to the repository

👉 Example:
For public orgs like `google`, collaborator data may not be returned.

---

## Tech Stack

* Java 17
* Spring Boot
* WebClient (Reactive HTTP client)
* Maven

---

## Project Structure

```
client/       → GitHub API calls  
service/      → Business logic & aggregation  
controller/   → REST endpoints  
model/        → DTOs  
config/       → WebClient configuration  
```

---

## Conclusion

This project demonstrates:

* Clean architecture
* Efficient API usage
* Scalable design
* Proper handling of real-world API limitations

---

## Author

Tejeshwini Ingalagi
