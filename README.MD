# Microservicio de Creación y Consulta de Usuarios

Este proyecto es un microservicio que permite la creación y consulta de usuarios. Utiliza Spring Boot y Gradle como herramientas de desarrollo.

## Endpoints

### /sign-up

Endpoint para crear un usuario nuevo. Se espera el siguiente contrato de entrada en formato JSON:

```
{
  "name": "Nombre",
  "email": "correo@ejemplo.com",
  "password": "a2asfGfdfdf4",
  "phones": [
    {
      "number": 87650009,
      "citycode": 7,
      "contrycode": "25"
    }
  ]
}
```

- El correo debe seguir la expresión regular que valida `aaaaaaa@undominio.algo`. En caso de no cumplir con el formato, se enviará un mensaje de error.
- La clave debe seguir la expresión regular `a2asfGfdfdf4`. Debe tener solo una mayúscula y solamente dos números (no necesariamente consecutivos), en combinación con letras minúsculas. Su longitud debe ser de 8 a 12 caracteres. En caso de no cumplir con el formato, se enviará un mensaje de error.
- El nombre y los teléfonos son campos opcionales.
- En caso de éxito, se retornará el usuario con los siguientes campos:
  - id: ID del usuario (puede ser generado por la base de datos o un Long).
  - created: Fecha de creación del usuario.
  - lastLogin: Fecha del último ingreso.
  - token: Token de acceso de la API (se utilizará JWT).
  - isActive: Indica si el usuario sigue habilitado dentro del sistema.

Si el usuario ya existe, se enviará un mensaje de error indicando que ya existe.

En caso de error en el endpoint, se retornará:

```
{
  "error": [
    {
      "timestamp": "Fecha y hora del error",
      "codigo": "Código de error",
      "detail": "Detalle del error"
    }
  ]
}
```

### /login

Endpoint para consultar la información del usuario. Debe utilizar el token generado en el endpoint /sign-up para realizar la consulta y retornara toda la información del usuario persistido. El token retornado cambiará en cada ejecución y se actualizará su caducidad.

El contrato de salida será:

```
{
  "id": 123456789,
  "created": "Fecha y hora de creación del usuario",
  "lastLogin": "Fecha y hora del último ingreso",
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWxpb0B0ZXN0...",
  "isActive": true,
  "name": "Nombre del Usuario",
  "email": "correo@ejemplo.com",
  "password": "a2asfGfdfdf4",
  "phones": [
    {
      "number": 87650009,
      "citycode": 7,
      "contrycode": "25"
    }
  ]
}
```

## Instrucciones de construcción y ejecución del proyecto

1. Clonar el repositorio desde GitHub: `git clone https://github.com/gstkein/glchallenge.git`
2. Navegar al directorio del proyecto: `cd glchallenge`
3. Construir el proyecto con Gradle: `gradle build`
4. Ejecutar el proyecto: `gradle bootRun`

El microservicio estará disponible en `http://localhost:8080`.

Se puede modificar application.properties para configurar la clave de jwt y la duración de los tokens.

## Diagrama de Componentes

Adjunto al proyecto se encuentra el diagrama de componentes en formato UML que muestra la estructura de los componentes utilizados en el microservicio.

## Diagrama de Secuencia

También se incluye el diagrama de secuencia en formato UML que describe las interacciones entre los componentes y los actores durante el proceso de creación y consulta de usuarios.

---

Ejemplo de uso de curl para realizar una solicitud al endpoint /sign-up:

```
curl -X POST http://localhost:8080/sign-up -H "Content-Type: application/json" -d '{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "password": "Abcd1234",
  "phones": [
    {
      "number": 87650009,
      "citycode": 7,
      "contrycode": "25"
    }
  ]
}'
```