# Fitness Center Management System

A full-stack fitness center management platform featuring a .NET backend API and native Android application built with Kotlin.

**Developer:** Lovro Lešić  
**Development Period:** April – July 2025

## Overview

This comprehensive fitness management system enables gym operators to manage their facilities, track member attendance, offer subscription packages, and engage with users through a feature-rich mobile application. The platform includes real-time attendance tracking, gamification through points and leaderboards, integrated messaging, and an e-commerce system.

## Key Features

### User Management & Authentication
- Secure JWT-based authentication with token renewal
- User registration and profile management
- Cloudflare-powered profile picture storage
- Role-based access control (admin operations)

### Fitness Center Operations
- Multi-center support with geolocation services
- Real-time occupancy tracking (users present in last 1.5 hours)
- Workout completion monitoring
- Distance-based center discovery
- Interactive map integration with navigation

### Membership & Subscriptions
- Flexible subscription package management
- Dynamic discount system
- Automated expiration tracking
- Membership extension capabilities
- Points-based reward system

### Attendance & Gamification
- Automatic check-in system
- Monthly calendar view of attendance history
- Consecutive visit tracking
- Center-wide leaderboards
- Visual analytics for attendance patterns

### Training Programs
- Trainer profile management with expertise listings
- Customizable training packages (goals, pricing, duration)
- Coach assignment to fitness centers

### Content Management
- Article publishing system with rich media support
- Image and text-based notifications
- Date-based content scheduling

### E-commerce
- Center-specific store management
- Dual-currency system (EUR and points)
- Purchase history tracking
- Inventory management

### Communication
- Direct messaging between users
- Group chat functionality
- Message read receipts
- Conversation management (create, leave, delete)
- Message pinning
- Participant management in group chats

## Architecture

### Backend (.NET)
- **Pattern:** Controller-Repository-Service architecture
- **Database:** SQLite with Entity Framework (Database First approach)
- **Authentication:** JWT Bearer tokens with claims-based authorization
- **Configuration:** IConfiguration for environment-specific settings
- **Data Transfer:** DTO pattern for clean API contracts
- **Async Operations:** Full async/await implementation for optimal performance

### Android Application (Kotlin)
- **Networking:** Retrofit with interceptor-based token injection
- **Architecture:** Repository pattern + ViewModel + UIState
- **Concurrency:** Kotlin Coroutines for non-blocking operations
- **State Management:** Flow-based reactive programming
- **Navigation:** Pre-loaded screens without NavController for instant transitions
- **UI:** Animated linear gradients with custom color cycles

## Technology Stack

### Backend
- .NET (ASP.NET Core)
- Entity Framework Core
- SQLite
- JWT Authentication
- DBeaver (database management)

### Android
- Kotlin
- Retrofit (HTTP client)
- Kotlin Coroutines & Flow
- ViewModel & LiveData
- Jetpack Compose (presumed from architecture)

### External Services
- Cloudflare Images (CDN and image storage)
- Google Maps API (location services)

## API Documentation

### Fitness Center Endpoints
```
GET    /api/FitnessCenter
GET    /api/FitnessCenter/{fitnessCenterId}
GET    /api/FitnessCenter/ClosestFitnessCentars
GET    /api/FitnessCenter/coaches/{fitnessCentarId}
GET    /api/FitnessCenter/PromoFitnessCentars
POST   /api/FitnessCenter/AddFitnessCenter
PUT    /api/FitnessCenter/UpdateFitnessCentar/{fitnessCentarId}
DELETE /api/FitnessCenter/DeleteFitnessCentar/{fitnessCentarId}
```

### Membership Endpoints
```
GET    /api/Membership/user
GET    /api/Membership/user/{fitnessCenterId}
GET    /api/Membership/FitnessCenter/{fitnessCenterId}
GET    /api/Membership/FitnessCenter/Leaderboard/{fitnessCenterId}
POST   /api/Membership/AddMembership
POST   /api/Membership/AddMembershipPackage
POST   /api/Membership/UpdateMembership
GET    /api/Membership/MembershipPackage/{membershipPackageId}
GET    /api/Membership/MembershipPackages/{fitnessCentarId}
PUT    /api/Membership/UpdateMembership/{membershipId}
DELETE /api/Membership/DeleteMembership/{membershipId}
```

### Coach Endpoints
```
GET    /api/Coach/{coachId}
GET    /api/Coach/coachProgram/{coachProgramId}
GET    /api/Coach/programs/{coachId}
POST   /api/Coach/AddCoach
POST   /api/Coach/AddCoachProgram
PUT    /api/Coach/UpdateCoach/{coachId}
DELETE /api/Coach/DeleteCoach/{coachId}
```

### Attendance Endpoints
```
GET    /api/Attendance/users
GET    /api/Attendance/users/{fitnessCenterId}
GET    /api/Attendance/fitnesscenters/{fitnessCenterId}
GET    /api/Attendance/fitnesscenters/recent/{fitnessCenterId}
GET    /api/Attendance/fitnesscenters/leaving/{fitnessCenterId}
POST   /api/Attendance/AddAttendance
PUT    /api/Attendance/UpdateAttendance/{attendanceId}
DELETE /api/Attendance/DeleteAttendance/{attendanceId}
```

### Article Endpoints
```
GET    /api/Article/articles/{fitnessCenterId}
POST   /api/Article/AddArticle
PUT    /api/Article/UpdateArticle/{articleId}
DELETE /api/Article/DeleteArticle/{articleId}
```

### Account Endpoints
```
GET    /api/Account/{userId}
POST   /api/Account/Register
POST   /api/Account/login
POST   /api/Account/UpdateUser
```

### Conversation & Messaging Endpoints
```
GET    /api/Conversation/{conversationId}
GET    /api/Conversation/chats
GET    /api/Conversation/{conversationId}/participants
GET    /api/Conversation/{conversationId}/search
POST   /api/Conversation/message/send/{recipientId}
POST   /api/Conversation/message/markAsRead
DELETE /api/Conversation/message/remove/{messageId}
DELETE /api/Conversation/remove/{conversationId}
PUT    /api/Conversation/message/update/{messageId}
GET    /api/Conversation/{conversationId}/message/unreadCount
GET    /api/Conversation/unreadCount
POST   /api/Conversation/createGroup
POST   /api/Conversation/{conversationId}/addParticipant
POST   /api/Conversation/{conversationId}/removeParticipant
POST   /api/Conversation/{conversationId}/leaveGroup
POST   /api/Conversation/pinMessage/{messageId}
```

### Shop Endpoints
```
GET    /api/Shop/items/{fitnessCenterId}
GET    /api/Shop/items/users
GET    /api/Shop/item/{shopItemId}
POST   /api/Shop/BuyShopItem/{shopItemId}
POST   /api/Shop/AddShopItem
PUT    /api/Shop/UpdateShopItem/{shopItemId}
DELETE /api/Shop/DeleteShopItem/{shopItemId}
```

## Design Philosophy

### Performance Optimization
- **Asynchronous Operations:** All database calls are async to handle network latency
- **Pre-loaded Navigation:** Screens and data pre-loaded to eliminate loading delays
- **Coroutine Architecture:** Prevents UI blocking and memory leaks

### User Experience
- **Animated Backgrounds:** Dynamic gradient animations on main screens
- **Instant Transitions:** No loading screens between pre-cached views
- **Real-time Updates:** Flow-based reactive data binding
- **Responsive Design:** Based on Figma specifications

### Security
- **JWT Tokens:** Secure, stateless authentication
- **Automatic Token Renewal:** Seamless session management
- **Bearer Token Interceptor:** Centralized authentication injection
- **Claims-based Authorization:** Fine-grained access control

## Development Timeline

| Phase | Period | Deliverables |
|-------|--------|--------------|
| Planning | Early April | ERD model, initial functional requirements |
| Design | April | Figma UI/UX specifications |
| Backend Foundation | April | Security implementation, database setup |
| API Development | April-May | Complete controller implementation |
| Android Core | May-June | Retrofit integration, repository layer |
| UI Implementation | June | Screen components and navigation |
| Integration | Late June | Full-stack integration and testing |

## Authentication Flow

1. User registers/logs in via `/api/Account/login`
2. Backend generates JWT token with email claim
3. Token stored on client and included in all subsequent requests
4. RetrofitBuilder interceptor automatically injects Bearer token
5. Backend validates token and extracts user identity from claims
6. Token auto-renewal on expiration

## Technical Highlights

### Message System Architecture
The messaging system supports both direct and group conversations through a sophisticated abstraction:
- **Conversations:** Represent chat destinations (1-on-1 or group)
- **Messages:** Linked to conversations, store sender information
- **Members:** Users connected to conversations
- **Read Status:** Per-user tracking via message-member relationships

### Attendance Intelligence
- Tracks users currently in facility (last 1.5 hours)
- Identifies users finishing workouts (1-1.5 hours)
- Provides visual analytics for center popularity
- Powers gamification through streak tracking

### Dual Currency Commerce
Items can be purchased using:
- **EUR:** Traditional currency
- **Points:** Earned through attendance and engagement

## Known Limitations (MVP)

- Role-based access uses admin email configuration instead of ASP.NET Identity roles
- Messaging uses polling (Flow) instead of WebSockets due to time constraints
- Hardcoded strings/colors instead of resources (prioritized Figma compliance)

## Future Enhancements

- Implement ASP.NET Identity for robust role management
- Migrate to WebSocket-based real-time messaging
- Internationalization (i18n) support
- Comprehensive resource management (strings.xml, colors.xml)
- Advanced analytics and reporting dashboard

## Author

**Lovro Lešić**

---

*This project demonstrates full-stack development capabilities including backend API design, mobile application development, database architecture, authentication systems, and modern Android development practices.*
