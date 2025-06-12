# 🗂️ CleanTaskManager

A modern Android Task Manager app developed using **Kotlin** and **Java**, following **MVVM** and **Clean Architecture** principles.  
The app allows users to manage tasks with local storage, API sync, reminders, background jobs, and settings with light/dark mode support.

---

## 📱 Screenshots

> *(link...)*

---

## ✨ Features

- ✅ View tasks from a remote API and store them locally
- 📝 Add, edit, delete tasks offline
- 🔄 Background sync with **WorkManager**
- ⏰ Task reminders using **AlarmManager**
- ⚙️ Settings screen (Dark Mode toggle) using **DataStore**
- 🌐 Asynchronous networking with **Retrofit** + **Coroutines**
- 💉 Dependency Injection with **Hilt**
- 🧪 Unit tests and instrumented tests
- 🎨 Material Design with support for dark theme
- 📦 Version-controlled dependencies using **Gradle Version Catalog**

---

## 🏛️ Architecture

This app is built using:

- **MVVM Pattern** – Clean separation between UI and business logic
- **Clean Architecture** – Layers: `presentation`, `domain`, `data`
- **Reactive Flow** – Using `Kotlin Flow` for data streams

> Layers:
> - **Presentation:** Activities, ViewModels, UI
> - **Domain:** UseCases, business logic interfaces
> - **Data:** Retrofit APIs, Room DAOs, Repositories

---

## 🛠️ Tech Stack

| Technology        | Purpose                        |
|------------------|--------------------------------|
| Kotlin + Java    | Programming Languages          |
| XML + Compose    | UI Design                      |
| MVVM             | Architecture Pattern           |
| Room             | Local Database                 |
| Retrofit         | REST API Client                |
| WorkManager      | Background Jobs                |
| AlarmManager     | Local Notifications/Reminders  |
| DataStore        | Key-Value Storage (Settings)   |
| Hilt             | Dependency Injection           |
| Kotlin Coroutines| Async Operations               |
| Kotlin Flow      | Reactive Data Streams          |
| Material 3       | Modern UI Components           |
| Git              | Version Control                |

---

## 🚀 How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/CleanTaskManager.git
