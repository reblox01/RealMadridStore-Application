# 🏟️ Real Madrid Store App

![Real Madrid]([https://upload.wikimedia.org/wikipedia/en/5/56/Real_Madrid_CF.svg](https://timelinecovers.pro/facebook-cover/download/fc-real-madrid-facebook-cover.jpg))

> An unofficial Real Madrid official store experience — built with Kotlin & Jetpack Compose 🤍

---

## 📱 About

The **Real Madrid Store App** is a full-featured Android shopping application dedicated to the most successful football club in history. Browse and purchase the latest 2025/26 jerseys, training gear, and accessories — all from your phone.

Powered by a local AI chatbot running **DeepSeek-R1** via Ollama, fans can ask anything about products, players, prices, and store locations — completely offline and free.

---

## ✨ Features

- 🛍️ **Shop** — Browse the full 2025/26 Real Madrid collection with real product images
- 🤖 **AI Chatbot** — Ask anything powered by DeepSeek-R1:8b running locally via Ollama
- 📸 **Camera in Chat** — Send photos to the AI directly from the chat screen
- 🗺️ **Store Locator** — Find official Real Madrid stores worldwide on Google Maps
- 📍 **Near Me** — Automatically find the closest store to your location
- 🛒 **Cart & Checkout** — Add products, manage quantities, and complete payment
- ❤️ **Wishlist** — Save your favorite products, persisted across sessions
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
| DataStore | Wishlist persistence |
| FusedLocationProvider | GPS / Near Me |

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
3. Add your API keys:
   - `AndroidManifest.xml` → Google Maps API Key
4. Start Ollama with DeepSeek
5. Hit ▶️ Run

---


## 👑 Hala Madrid!

Built with ❤️ by **Zayd Kassimi**
