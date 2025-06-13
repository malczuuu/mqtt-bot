# MQTT Bot

This application is a Spring Boot-based MQTT simulator that allows users to:

1. ✅ **Create and manage multiple simulated MQTT devices**
2. 🕒 **Schedule messages that will be sent to MQTT topics**
3. 🔄 **Automatically manage MQTT connections for each device**

## Features

- 🔧 Create, update, and delete MQTT devices with per-device topic and payload configuration.
- 📅 Schedule messages with CRON or fixed interval expressions.
- 📡 Connects to MQTT brokers using Eclipse Paho library.
- 🔌 Automatically handles device MQTT connections (connect, reconnect, disconnect).
- 💾 Persists device and schedule data in MongoDB.
