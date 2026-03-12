# 🐾 Animal Shelter API

A RESTful API built with Java and Spring Boot to manage an animal shelter system.
This project handles animal registration, tracking, and adoption status management.

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**
- **Lombok**

## 📋 Prerequisites

Before running this project make sure you have installed:

- Java 17 or higher
- Maven
- PostgreSQL
- Postman (optional - for testing endpoints)

## ⚙️ How to Run Locally

1. Clone the repository
```bash
git clone https://github.com/Jerryhdezdev/animal-shelter-backend.git
```

2. Create the database in PostgreSQL
```sql
CREATE DATABASE animal_shelter;
```

3. Configure your database credentials in `src/main/resources/application.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/animal_shelter
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD_HERE
```

4. Run the application
```bash
mvn spring-boot:run
```

5. The API will be available at
```
http://localhost:8080
```

## 📡 API Endpoints

### Animals

| Method | Endpoint | Description | Status Code |
|--------|----------|-------------|-------------|
| GET | /api/v1/animals | Get all animals | 200 |
| GET | /api/v1/animals/{id} | Get animal by id | 200 |
| POST | /api/v1/animals | Create new animal | 201 |
| PUT | /api/v1/animals/{id} | Update animal | 200 |
| DELETE | /api/v1/animals/{id} | Delete animal | 204 |

### Request Body Example — POST / PUT
```json
{
    "name": "Max",
    "animalSpecies": "DOG",
    "animalSex": "MALE",
    "birthDate": "2021-05-10",
    "weight": 25.5,
    "animalSize": "LARGE",
    "animalVaccinationStatus": "FULL_VACCINATED",
    "animalSterilizationStatus": "YES",
    "description": "Max is a friendly and energetic dog who loves to play"
}
```

### Response Example
```json
{
    "id": 1,
    "name": "Max",
    "species": "DOG",
    "sex": "MALE",
    "birthDate": "2021-05-10",
    "weight": 25.5,
    "size": "LARGE",
    "vaccinationStatus": "FULL_VACCINATED",
    "sterilizationStatus": "YES",
    "adoptionStatus": "INTAKE_ASSESSMENT",
    "intakeDate": "2026-03-11",
    "description": "Max is a friendly and energetic dog who loves to play"
}
```

### Error Response Example
```json
{
    "status": 404,
    "error": "NOT_FOUND",
    "message": "Animal not found with id: 999",
    "timestamp": "2026-03-11T21:30:00"
}
```

## 📁 Project Structure
```
src/main/java/com/jerryhdez/animalshelter/
│
├── domain/
│   ├── model/
│   │   ├── Animal.java
│   │   ├── AdoptionStatus.java
│   │   ├── AnimalSex.java
│   │   ├── AnimalSize.java
│   │   ├── AnimalSpecies.java
│   │   ├── AnimalSterilizationStatus.java
│   │   └── AnimalVaccinationStatus.java
│   ├── repository/
│   │   └── AnimalRepository.java
│   └── service/
│       └── AnimalService.java
│
├── exception/
│   ├── AnimalNotFoundException.java
│   ├── ErrorResponseDTO.java
│   └── GlobalExceptionHandler.java
│
└── web/
    ├── controller/
    │   └── AnimalController.java
    ├── dto/
    │   ├── AnimalRequestDTO.java
    │   └── AnimalResponseDTO.java
    └── mapper/
        └── AnimalMapper.java
```

## 👨‍💻 Author

**JerryHdez**
- GitHub: [@jerryhdezdev](https://github.com/Jerryhdezdev)

---
⭐ If you find this project useful, please give it a star on GitHub!