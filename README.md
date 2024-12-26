# iPhone Marketplace Telegram Web App

A Telegram web application for buying and selling iPhones, built with Spring Boot and PostgreSQL.

## Features

- User authentication via Telegram
- Advertisement posting and management
- Real-time chat functionality
- Payment system integration
- Technical inspection module with IMEI verification
- Admin panel for monitoring and management

## Tech Stack

- Backend: Spring Boot (Java)
- Database: PostgreSQL
- Frontend: Telegram Bot API, HTML/CSS
- Cloud: AWS/DigitalOcean ready
- Payment Integration: Click, Payme, Paynet

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- PostgreSQL
- Telegram Bot Token

### Installation

1. Clone the repository
2. Configure application.properties with your database and Telegram credentials
3. Run `mvn clean install`
4. Start the application with `mvn spring-boot:run`

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/iphone/marketplace/
│   │       ├── config/
│   │       ├── controller/
│   │       ├── model/
│   │       ├── repository/
│   │       └── service/
│   └── resources/
│       └── application.properties
└── test/
    └── java/
```
