# IoT Control App

Proyecto intermodular FP DAM para monitorización IoT mediante aplicación Android, backend Spring Boot y base de datos PostgreSQL.

## Tecnologías utilizadas

- Android Kotlin
- Spring Boot
- PostgreSQL
- Retrofit
- MPAndroidChart
- API REST
- Maven

## Funcionalidades principales

- Registro e inicio de sesión
- Roles USER / ADMIN
- Gestión de usuarios
- Registro RFID simulado
- Medición instantánea individual
- Medición instantánea simultánea
- Monitorización continua individual
- Monitorización continua simultánea
- Historial de sesiones
- Gráficos en tiempo real
- Exportación PDF
- Exportación CSV

## Arquitectura

El proyecto está dividido en tres partes principales:

- `/app` → aplicación Android
- `/backend` → backend Spring Boot
- `/database` → scripts SQL de base de datos

## Configuración de la base de datos

### 1. Crear base de datos PostgreSQL

Crear una base de datos llamada:

```sql
iot_db
```

### 2. Restaurar estructura de tablas

Ejecutar:

```bash
psql -U postgres -d iot_db -f database/schema.sql
```

### 3. Insertar datos iniciales

Ejecutar los archivos SQL de la carpeta `/database`.

Ejemplo:

```bash
psql -U postgres -d iot_db -f database/sensor_types_data.sql
psql -U postgres -d iot_db -f database/sensors_data.sql
psql -U postgres -d iot_db -f database/devices_data.sql
psql -U postgres -d iot_db -f database/users.sql
```

## Configuración del backend

Editar:

`backend/src/main/resources/application.properties`

y configurar usuario y contraseña de PostgreSQL.

## Ejecución del backend

Desde la carpeta `/backend` ejecutar:

```bash
./mvnw spring-boot:run
```

El backend se ejecuta en:

`http://localhost:8080`

## Ejecución de la aplicación Android

Abrir la carpeta `/app` en Android Studio y ejecutar la aplicación.

## Simulación de sensores

Actualmente las mediciones se generan mediante valores simulados aleatorios.

La arquitectura está preparada para integrar sensores reales mediante ESP32 o Raspberry Pi utilizando peticiones HTTP JSON.

## Autora

Proyecto desarrollado por Nastasia Zubareva.
