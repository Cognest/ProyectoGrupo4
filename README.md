# Cognest — Plataforma de compra/venta de contenido multimedia

**Resumen rápido**  
Plataforma web para la compra/venta de contenido multimedia (imágenes, vídeos, audios, modelos 3D).
- Los usuarios pueden registrarse/iniciar sesión, comprar **tokens** (moneda interna) y usar tokens para adquirir contenido publicado por otros.
- Se soporta interacción social: **like**, **guardar**, **comentar**, **chat** (historial + websocket), **bloquear** usuarios y **reportar** contenido.
- Registro de movimientos de tokens en una **cartera** (historial de transacciones).
- Backend en **Spring Boot**, vistas con plantillas (Thymeleaf) y APIs REST/JSON para funcionalidades dinámicas.

---

## Índice
1. [Características](#1-características-principales)
2. [Estructura del proyecto](#2-estructura-del-proyecto-reseña)
3. [Entidades principales](#3-entidades-clave-resumen-conceptual)
4. [Endpoints importantes](#4-endpoints-importantes--ejemplos-reales-extraídos-del-proyecto)
5. [Flujo de tokens / compra de contenido](#5-flujo-de-tokens-y-reparto-en-compra-resumen)
6. [Configuración y ejecución local](#6-cómo-ejecutar-local)
7. [Pruebas y calidad](#7-pruebas-y-calidad)
8. [CI / Despliegue](#8-ci--despliegue)
9. [Notas y mejoras](#9-notas-mejoras-y-extensiones-recomendadas)

---

## 1. Características principales
- Registro / Login / Perfil de usuario.
- Subida y gestión de contenidos multimedia (categorías, etiquetas, previews).
- Sistema de **tokens**:
    - Compra de tokens (página de planes / pago).
    - Gastos en tokens para adquirir contenido.
    - Registro de ingresos/gastos en la **cartera** del usuario.
- Carrito de compra: añadir/quitar elementos, finalizar compra (verifica tokens disponibles y atiende reparto).
- Interacciones sociales: likes, guardados, comentarios.
- Chat privado entre usuarios con historial (API) y WebSocket para mensajes en tiempo real.
- Moderación: reporte de contenido y bloqueo de usuarios.
- Descarga de contenido comprado (control de accesos).

---

## 2. Estructura del proyecto (reseña)
- `controllers` — controladores (web + REST).
    - `CarritoController` — gestión del carrito y finalización de compra.
    - `PlanesController` — vistas y procesamiento de pagos / compra de tokens.
    - `DetallesContenidoController` — ver contenido, like, guardar, comentar, descargar.
    - `ChatRestController` & `ChatWebSocketController` — API de historial y WebSocket para chat.
    - `VistaUsuarioController` — bloqueo/desbloqueo usuarios, vistas de perfil.
- `entities` — entidades JPA: `Usuario`, `Contenido`, `UsuarioContenido`, `Cartera`, `Chat`, etc.
- `repositories` — interfaces `JpaRepository`.
- `services` — lógica de negocio: `CarteraService`, `LikeService`, `GuardadoService`, `ChatService`, etc.
- `templates` — vistas Thymeleaf (`carrito`, `cartera`, `planes`, etc.).
- `static` — JS estático (`actualizarTokens.js`, etc.).
- `application.properties` — configuración de la aplicación.

---

## 3. Entidades clave (resumen conceptual)
- **Usuario**: nickname, email, password (codificada), token (saldo de tokens), rol, etc.
- **Contenido**: metadata del archivo (tipo, precio en tokens, autor, rutas de almacenamiento).
- **UsuarioContenido**: relación usuario–contenido con estado (`Pendiente`, `Comprado`, `Creador`, etc.).
- **Cartera**: registro de transacciones (cantidad, saldoActual, operación `ingreso`/`egreso`, fecha).
- **Chat**: mensajes entre usuarios con emisor, receptor, texto y fecha.

---

## 4. Endpoints importantes — ejemplos reales extraídos del proyecto

### Carrito
- `GET  /carrito` — Vista del carrito.
- `POST /carrito/anadir` — Añade contenido al carrito.
- `POST /carrito/eliminar` — Elimina elemento del carrito.
- `POST /carrito/finalizar` — Finaliza compra (valida tokens, registra en `Cartera`, transfiere ingresos a vendedores).

```bash
curl -X POST "https://tu-host/carrito/finalizar"   -u usuario:password   -d "contenidoIds=12&contenidoIds=34"   -d "totalCompra=50"
```

---

### Compra de tokens / planes
- `GET  /planes` — Lista de planes disponibles de tokens.
- `GET  /pagar` — Muestra formulario de pago para seleccionar un plan.
- `POST /procesar-pago` — Procesa la compra de tokens y registra la operación en la entidad `Cartera`.

**Ejemplo de flujo:**
1. Usuario selecciona un plan (por ejemplo, 100 tokens).
2. Se envía un POST a `/procesar-pago` con:
   ```form
   tokens=100
   ```
3. El servidor:
    - Incrementa el saldo de tokens del usuario.
    - Crea un registro en `Cartera` con `operacion = ingreso`.
    - Guarda el nuevo saldo y muestra la vista `planes/pagoExitoso`.

---

### Guardar / Like / Comentarios
- `POST /guardar` — Guarda o desmarca contenido como favorito.
    - Respuesta típica: `{ "guardado": true }` o `{ "guardado": false }`.
- `POST /like` — Añade o quita un like a un contenido.
    - Respuesta típica: `{ "liked": true, "totalLikes": 15 }`.
- `POST /comentarios` — Publica un comentario sobre el contenido.

**Ejemplo de uso vía JavaScript (guardar contenido):**
```javascript
fetch('/guardar', {
  method: 'POST',
  body: new URLSearchParams({ contenidoId: 123 })
}).then(res => res.json())
  .then(data => console.log(data.guardado));
```

---

### Chat
- `GET /api/chat/historial/{nickname}` — Devuelve el historial de mensajes entre el usuario autenticado y el usuario `{nickname}`.

**Ejemplo de respuesta JSON:**
```json
[
  {
    "id": 1,
    "emisor": "alice",
    "receptor": "bob",
    "mensaje": "Hola, ¿está disponible?",
    "fecha": "2025-08-09T20:15:30"
  },
  {
    "id": 2,
    "emisor": "bob",
    "receptor": "alice",
    "mensaje": "Sí, todavía lo tengo.",
    "fecha": "2025-08-09T20:16:05"
  }
]
```

También se incluye soporte WebSocket (`ChatWebSocketController`) para enviar y recibir mensajes en tiempo real usando STOMP.

---

### Descarga de contenido
- `GET /contenido/{id}/descargar` — Permite descargar un contenido si el usuario tiene permisos (lo compró o es el autor).

---

### Moderación
- `POST /bloquear-usuario` — Bloquea a un usuario para evitar interacciones.
- `POST /desbloquear-usuario` — Quita un bloqueo.
- `POST /reportar` — Envía un reporte sobre un contenido para su revisión por moderadores.

---

## 5. Flujo de tokens y reparto en compra (resumen)
1. El usuario compra tokens en `/procesar-pago` → se incrementa su saldo y se registra en `Cartera` como ingreso.
2. Añade contenido al carrito y finaliza la compra en `/carrito/finalizar`.
3. Se verifica que dispone de tokens suficientes.
4. Se descuentan tokens del comprador y se registra un `Cartera` con `operacion = egreso`.
5. Se registra un `Cartera` con `operacion = ingreso` para cada vendedor involucrado.
6. Se actualiza el saldo de todos los implicados y se marcan los contenidos como adquiridos.

---

## 6. Cómo ejecutar (local)

### Requisitos
- JDK 17+
- Maven
- Base de datos PostgreSQL (o la que configures en `application.properties`)

### Pasos
```bash
mvn clean spring-boot:run
```
O para ejecutar un jar:
```bash
mvn clean package
java -jar target/tu-app.jar
```

---

## 7. Pruebas y calidad
- **Testcontainers** para pruebas de integración:
```bash
mvn test
```
- **SonarQube** para análisis de calidad de código.

---

## 8. CI / Despliegue
- Recomendado uso de **GitHub Actions** para:
    - Ejecutar pruebas automáticas.
    - Compilar y empaquetar el jar.
    - Analizar con SonarQube.
    - Crear y subir imagen Docker.
    - Desplegar en entornos de staging o producción.

---

## 9. Notas, mejoras y extensiones recomendadas
- Integrar pagos reales (Stripe, PayPal) con validación de webhooks.
- Almacenamiento en S3 + CDN para escalabilidad.
- Auditoría completa de compras y reportes.
- Tests E2E con Selenium o Playwright para los flujos críticos.
- Documentar API con Swagger/OpenAPI.

---
