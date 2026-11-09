# ♟️ Chess App

A modern Android chess application built with **Kotlin** and **Jetpack Compose**.


## ✨ Features
* **Local Multiplayer**: Play against a friend on the same device.
* **Standard Chess Rules**: Full move validation including castling, en passant, and pawn promotion.
* **Modern UI**: Fully declarative user interface built from scratch using Jetpack Compose.
* **Responsive Design**: The chess board automatically scales to fit different Android screen sizes perfectly.

## 🏗️ Architecture
This project is built using the **MVC (Model-View-Controller)**[cite: 15] architectural pattern adapted for modern declarative UI:
* **Model**: Contains the core business logic, chess algorithms, piece movement rules, and the state of the board[cite: 15].
* **View**: Composable functions in Jetpack Compose that observe the state and render the visual representation of the game.
* **Controller**: Activity classes that capture user interactions (taps on the board) and trigger updates in the Model[cite: 15].

## 🛠️ Tech Stack
* **Language**: **Kotlin**[cite: 15]
* **IDE**: **Android Studio**[cite: 15]
* **UI Toolkit**: Jetpack Compose
* **Build System**: Gradle (Kotlin DSL)

## 🚀 Installation & Setup
To run this project on your local machine, follow these steps:

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Denisfis/Chess.git
