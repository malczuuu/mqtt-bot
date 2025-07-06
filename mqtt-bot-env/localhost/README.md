# Services setup of MQTT Bot for localhost

## Table of Contents

* [Summary](#summary)
* [RabbitMQ](#rabbitmq)

## Summary

This setup can be launched on local machine with following command.

```bash
docker-compose up -d
```

> **Note:** In newer versions of Docker, `docker-compose` (the standalone CLI tool) has been deprecated and is now
> integrated into the Docker CLI as `docker compose`.
>
> To maintain compatibility with existing scripts, you can add the following alias to your `~/.bashrc`:
>
> ```bash
> alias docker-compose="docker compose"
> ```

## RabbitMQ

This repository sets up RabbitMQ message broker (version `4.1.0`) with enabled MQTT plugin and administrator dashboard.

Predefined users:

* `admin:admin` - for administration dashboard (http://localhost:15672/)
* `user:user` - for MQTT connectivity (tcp://localhost:1883)

To test the connection user `mosquitto-sub` and `mosquitto-pub` applications.

> To install these applications make sure to install `mosquitto-clients`. Do not install `mosquitto` package as it will
> install also [Mosquitto](https://github.com/eclipse-mosquitto/mosquitto) MQTT server which will conflict ports with
> your RabbitMQ MQTT ports.
>
> ```bash
> sudo apt-get install mosquitto-clients
> ```

1. In first terminal subscribe to any topic with `user:user` credentials.

   ```bash
   mosquitto_sub -h localhost -p 1883 -u user -P user -t topic/subtopic
   ```

2. In second terminal publish message to the subscribed topic with `user:user` credentials.

   ```bash
   mosquitto_pub -h localhost -p 1883 -t topic/subtopic -u user -P user -m 'hello world'
   ```
