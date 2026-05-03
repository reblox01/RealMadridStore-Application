# 🏟️ Real Madrid Store App

![Real Madrid](https://timelinecovers.pro/facebook-cover/download/fc-real-madrid-facebook-cover.jpg)

> An unofficial Real Madrid official store experience — built with Kotlin & Jetpack Compose 🤍

---

## 📱 About

The **Real Madrid Store App** is a full-featured Android shopping application dedicated to the most successful football club in history. Browse and purchase the latest 2025/26 jerseys, training gear, and accessories — all from your phone.

Powered by a local AI chatbot running **DeepSeek-R1** via Ollama, fans can ask anything about products, players, prices, and store locations — completely offline and free.

The app uses **Firebase Firestore** as its cloud database and backend, ensuring products, orders, and wishlists are stored and synced in real time.

---

## ✨ Features

- 🛍️ **Shop** — Browse the full 2025/26 Real Madrid collection with real product images
- 🤖 **AI Chatbot** — Ask anything powered by DeepSeek-R1:8b running locally via Ollama
- 📸 **Camera in Chat** — Send photos to the AI directly from the chat screen
- 🗺️ **Store Locator** — Find official Real Madrid stores worldwide on Google Maps
- 📍 **Near Me** — Automatically find the closest store to your location
- 🛒 **Cart & Checkout** — Add products, manage quantities, and complete payment
- ❤️ **Wishlist** — Save your favorite products, synced to the cloud
- 🔥 **Firebase Backend** — Real cloud database powering products, orders and wishlists
- 🏆 **Premium UI** — Real Madrid Navy, White & Gold design system

---

## 🛠️ Tech Stack

| Technology | Usage |
|---|---|
| Kotlin | Primary language |
| Jetpack Compose | UI framework |
| MVVM + StateFlow | Architecture |
| Retrofit | Network calls |
| Ollama + DeepSeek-R1:8b | Local AI chatbot |
| Google Maps Compose | Store locator |
| Coil | Image loading |
| Firebase Firestore | Cloud database & backend |
| Firebase Auth | User authentication |
| DataStore | Local persistence |
| FusedLocationProvider | GPS / Near Me |

---

## 🗄️ Database & Backend

This app uses **Firebase Firestore** as its cloud database and backend:

- 📦 Products stored and fetched from Firestore cloud database
- 🛒 Orders saved to Firestore after every successful payment
- ❤️ Wishlist synced per user to the cloud
- 🔒 Secured with Firebase security rules

---

## 🔒 Security

- API keys stored in `secrets.properties` (not pushed to GitHub)
- `google-services.json` excluded from version control
- Firebase security rules enabled
- `.gitignore` configured to protect all sensitive files

---

## 🤖 AI Chatbot Setup

The chatbot runs **100% locally** on your machine — no API costs!

1. Install [Ollama](https://ollama.com)
2. Run:
```bash
ollama run deepseek-r1:8b
```
3. The app connects to `http://10.0.2.2:11434` automatically

---

## 🚀 Getting Started

1. Clone the repo:
```bash
git clone https://github.com/zaydkassimi/RealMadridStore-Application.git
```
2. Open in **Android Studio**
3. Create `secrets.properties` in the project root:
```
MAPS_API_KEY=your_google_maps_api_key_here
```
4. Add `google-services.json` from your Firebase Console to the `app/` folder
5. Enable Firestore in Firebase Console
6. Start Ollama with DeepSeek
7. Hit ▶️ Run

---

## 👑 Hala Madrid!

Built with ❤️ by **Zayd Kassimi**
