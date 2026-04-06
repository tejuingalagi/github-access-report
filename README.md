# GitHub Access Report Service

## Problem Statement

Organizations often need visibility into who has access to which repositories in GitHub.
This project provides a backend service that connects to GitHub APIs, retrieves repository and collaborator data, and generates a structured access report showing which users have access to which repositories.

---

## Features

* Secure authentication with GitHub using Personal Access Token (PAT)
* Fetch repositories for a given organization or user
* Retrieve collaborators for each repository
* Determine access levels (ADMIN / WRITE / READ)
* Aggregate data to map users → repositories
* Expose REST API endpoint to fetch report in JSON format
* Handles both **GitHub organizations and individual users**

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
    "username": "user1",
    "repositories": [
      {
        "repository": "repo-name",
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

---

### 2. Configure GitHub Token

Update `application.yaml`:

```yaml
github:
  base-url: https://api.github.com
  token: YOUR_GITHUB_PERSONAL_ACCESS_TOKEN
```

👉 Generate token from:
GitHub → Settings → Developer Settings → Personal Access Tokens

---

### 3. Run the Application

```
mvn spring-boot:run
```

Or run the main class from your IDE.

---

### 4. Test the API

```
http://localhost:8080/api/github/access-report?org=your-org
```

---

## Swagger UI

```
http://localhost:8080/swagger-ui.html
```

---

## Authentication

* Uses GitHub Personal Access Token (PAT)
* Token is passed in header:

```
Authorization: Bearer <token>
```

* Ensures secure communication with GitHub APIs

---

## Design & Implementation

### 1. API Integration

* `/orgs/{org}/repos` → fetch organization repositories
* `/users/{user}/repos` → fallback for user repositories
* `/repos/{org}/{repo}/collaborators` → fetch collaborators

---

### 2. Access Level Mapping

* `admin = true` → ADMIN
* `push = true` → WRITE
* `pull = true` → READ

---

### 3. Aggregation Logic

* Data is transformed into:

```
User → List of Repositories + Access
```

* Uses `ConcurrentHashMap` for thread-safe aggregation

---

### 4. Performance (Scale Handling)

* Uses `parallelStream()` for concurrent processing of repositories
* Avoids sequential API calls
* Designed to support:

  * 100+ repositories
  * 1000+ users

---

### 5. Error Handling

* Handles API errors using `onStatus()` in WebClient
* Prevents application crashes on failures
* Returns empty responses in case of partial failures

---

## Assumptions & Limitations

* Supports both GitHub organizations and individual users
* GitHub API may restrict collaborator data for public repositories
* Requires appropriate permissions in the provided token
* Pagination is currently limited to 100 results per API call
* WebClient is used with blocking (`.block()`) for simplicity; can be made fully reactive in production

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

* Clean architecture and separation of concerns
* Efficient API usage
* Scalable design using parallel processing
* Proper handling of real-world API limitations

---

## Author

Tejeshwini Ingalagi
