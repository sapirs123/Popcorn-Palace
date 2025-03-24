
# Popcorn Palace API

Popcorn Palace is a RESTful API service for managing a movie theater system. It allows theaters to manage movies, showtimes, theaters, and customer bookings.

## Technologies

- Java 21
- Spring Boot 3.4.2
- Spring Data JPA
- PostgreSQL
- Lombok
- Maven

## Project Structure

The application follows a standard layered architecture:

- **Controllers**: Handle HTTP requests and responses
- **Services**: Implement business logic
- **Repositories**: Data access layer
- **DTOs**: Data transfer objects for API requests/responses
- **Entities**: JPA entities that map to database tables
- **Exceptions**: Custom exception handling

## Features

### Movies
- Create, read, update, and delete movies
- Search movies by ID
- Update partial movie information

### Theaters
- Create, read, update, and delete theaters
- Search theaters by name
- Check theater capacity

### Showtimes
- Schedule movie showings in theaters
- Prevent scheduling conflicts for theaters
- Validate that showtime durations match movie length

### Bookings
- Create, read, update, and delete bookings
- Track seats sold for each showtime
- Prevent overbooking based on theater capacity
- Associate bookings with users

## API Endpoints

### Movies
- `GET /api/movies/all` - Get all movies
- `GET /api/movies/{id}` - Get movie by ID
- `POST /api/movies` - Create movie
- `PUT /api/movies/{id}` - Update movie
- `POST /api/movies/title/{movieTitle}` - Update movie by title
- `DELETE /api/movies/{id}` - Delete movie
- `DELETE /api/movies/title/{title}` - Delete movie by title

### Theaters
- `GET /api/theaters` - Get all theaters
- `GET /api/theaters/{name}` - Get theater by name
- `POST /api/theaters` - Create theater
- `PUT /api/theaters/{name}` - Update theater
- `DELETE /api/theaters/{name}` - Delete theater

### Showtimes
- `GET /api/showtimes` - Get all showtimes
- `GET /api/showtimes/{id}` - Get showtime by ID
- `POST /api/showtimes` - Create showtime
- `PUT /api/showtimes/{id}` - Update showtime
- `DELETE /api/showtimes/{id}` - Delete showtime

### Bookings
- `GET /api/bookings` - Get all bookings
- `GET /api/bookings/{id}` - Get booking by ID
- `GET /api/bookings/showtime/{showtimeId}` - Get bookings by showtime
- `POST /api/bookings` - Create booking
- `PUT /api/bookings/{id}` - Update booking
- `DELETE /api/bookings/{id}` - Delete booking

## Setup and Running

### Prerequisites
- Java 21
- PostgreSQL
- Maven or Docker and Docker Compose

### Using Maven

1. Clone the repository:
   ```
   git clone https://github.com/sapirs123/Popcorn-Palace.git
   cd popcorn-palace
   ```

2. Configure database connection in `src/main/resources/application.properties`

3. Start the application using Docker Compose:
   ```
   docker-compose up
   ```

4. Build and run the application:
   ```
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```


The application will be accessible at `http://localhost:8080`

## Data Model

### Movie
- id: Long (PK)
- title: String (unique)
- genre: String
- duration: Integer (minutes)
- rating: Double (0-10)
- releaseYear: Integer (1900-2025)

### Theater
- id: Long (PK)
- name: String (unique)
- capacity: Integer
- location: String

### Showtime
- id: Long (PK)
- movieId: Long (FK)
- theaterName: String (FK)
- startTime: LocalDateTime
- endTime: LocalDateTime
- price: Double

### Booking
- id: Long (PK)
- showtimeId: Long (FK)
- userId: Long
- email: String
- numberOfSeats: Integer
- bookingTime: LocalDateTime

## Error Handling

The API provides appropriate error responses for various scenarios:
- Resource not found errors
- Validation errors
- Conflict errors (e.g., showtime conflicts, seat availability)
- General server errors
