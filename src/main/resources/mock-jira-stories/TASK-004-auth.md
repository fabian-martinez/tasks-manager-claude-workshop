# TASK-004: Implementar Autenticación JWT

## 📋 User Story

**Como** administrador del sistema y usuario final
**Quiero** que la aplicación tenga un sistema de autenticación seguro basado en JWT
**Para** proteger los datos y garantizar que solo usuarios autorizados puedan acceder a las funcionalidades del sistema

## 🎯 Objetivos de Negocio

- Implementar seguridad robusta para proteger datos sensibles
- Controlar acceso granular a diferentes funcionalidades
- Cumplir con estándares de seguridad empresariales
- Preparar la base para futura implementación de roles y permisos
- Habilitar integración segura con sistemas externos

## 📝 Descripción Detallada

Actualmente la aplicación no tiene autenticación, lo que la hace insegura para uso en producción. Se necesita implementar un sistema completo de autenticación basado en JWT (JSON Web Tokens) que incluya registro, login, renovación de tokens, y protección de endpoints.

## ✅ Criterios de Aceptación

### Autenticación Básica
- [ ] **Registro de usuarios**
  - Endpoint para crear nuevas cuentas de usuario
  - Validación de email único y contraseña segura
  - Hash seguro de contraseñas (BCrypt)
  - Verificación de email opcional

- [ ] **Login de usuarios**
  - Autenticación con email y contraseña
  - Generación de JWT token con claims apropiados
  - Refresh token para renovación automática
  - Rate limiting para prevenir ataques de fuerza bruta

- [ ] **Gestión de tokens**
  - Tokens JWT con expiración configurable
  - Refresh tokens de larga duración
  - Blacklist de tokens revocados
  - Logout que invalide tokens

### Protección de Endpoints
- [ ] **Middleware de autenticación**
  - Interceptor que valide JWT en requests
  - Extracción de información del usuario del token
  - Manejo de tokens expirados y inválidos
  - Headers de autenticación estándar (Authorization: Bearer)

- [ ] **Endpoints públicos y privados**
  - Endpoints de autenticación públicos (/auth/*)
  - Todos los endpoints de API protegidos
  - Documentación clara de qué requiere autenticación
  - Mensajes de error apropiados (401, 403)

### Autorización Básica
- [ ] **Contexto del usuario autenticado**
  - Información del usuario actual disponible en controllers
  - Validación que usuarios solo accedan a sus datos
  - Propietario de tareas puede ver/editar sus tareas
  - Base para futura implementación de roles

## 🔧 Especificación Técnica

### Nuevos Endpoints

#### Autenticación
```
POST /auth/register          - Registro de nuevo usuario
POST /auth/login            - Login con email/password
POST /auth/refresh          - Renovar token con refresh token
POST /auth/logout           - Logout e invalidar tokens
GET  /auth/me               - Información del usuario actual
PUT  /auth/change-password  - Cambiar contraseña
```

### Modelos de Datos

#### User Entity (Modificado)
```java
@Entity
public class User {
    // ... campos existentes ...

    @Column(nullable = false)
    private String password; // Hashed with BCrypt

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified = false;

    @Column(name = "account_locked", nullable = false)
    private boolean accountLocked = false;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "failed_login_attempts")
    private int failedLoginAttempts = 0;
}
```

#### RefreshToken Entity
```java
@Entity
public class RefreshToken {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false)
    private boolean revoked = false;
}
```

### DTOs para Autenticación

#### Requests
```java
public class LoginRequestDto {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;
}

public class RegisterRequestDto {
    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8, max = 128)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]")
    private String password;
}
```

#### Responses
```java
public class AuthResponseDto {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresIn; // seconds
    private UserProfileDto user;
}

public class UserProfileDto {
    private Long id;
    private String name;
    private String email;
    private boolean emailVerified;
    private LocalDateTime lastLogin;
}
```

### JWT Configuration

#### JWT Claims
```json
{
  "sub": "user@example.com",
  "userId": 123,
  "name": "John Doe",
  "iat": 1643723400,
  "exp": 1643809800,
  "iss": "task-manager-api"
}
```

#### Security Configuration
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
```

## 🗂️ Archivos a Crear

### Autenticación y Seguridad
- `src/main/java/com/workshop/taskmanager/security/JwtAuthenticationFilter.java`
- `src/main/java/com/workshop/taskmanager/security/JwtTokenProvider.java`
- `src/main/java/com/workshop/taskmanager/security/UserPrincipal.java`
- `src/main/java/com/workshop/taskmanager/security/CustomUserDetailsService.java`

### Servicios
- `src/main/java/com/workshop/taskmanager/service/AuthService.java`
- `src/main/java/com/workshop/taskmanager/service/JwtService.java`
- `src/main/java/com/workshop/taskmanager/service/RefreshTokenService.java`

### Modelos
- `src/main/java/com/workshop/taskmanager/model/RefreshToken.java`

### DTOs
- `src/main/java/com/workshop/taskmanager/dto/LoginRequestDto.java`
- `src/main/java/com/workshop/taskmanager/dto/RegisterRequestDto.java`
- `src/main/java/com/workshop/taskmanager/dto/AuthResponseDto.java`
- `src/main/java/com/workshop/taskmanager/dto/UserProfileDto.java`
- `src/main/java/com/workshop/taskmanager/dto/ChangePasswordRequestDto.java`

### Controladores
- `src/main/java/com/workshop/taskmanager/controller/AuthController.java`

### Repositorios
- `src/main/java/com/workshop/taskmanager/repository/RefreshTokenRepository.java`

### Configuración
- `src/main/java/com/workshop/taskmanager/config/SecurityConfig.java`

### Excepciones
- `src/main/java/com/workshop/taskmanager/exception/AuthenticationException.java`
- `src/main/java/com/workshop/taskmanager/exception/TokenExpiredException.java`
- `src/main/java/com/workshop/taskmanager/exception/InvalidTokenException.java`

### Tests
- `src/test/java/com/workshop/taskmanager/service/AuthServiceTest.java`
- `src/test/java/com/workshop/taskmanager/security/JwtTokenProviderTest.java`
- `src/test/java/com/workshop/taskmanager/controller/AuthControllerTest.java`

## 🗄️ Archivos a Modificar

### Modelos Existentes
- `User.java` - Agregar campos de autenticación y seguridad
- `UserRepository.java` - Métodos para autenticación

### Servicios Existentes
- `UserService.java` - Integrar con autenticación
- `TaskService.java` - Validar propiedad de tareas
- `CommentService.java` - Validar permisos de comentarios

### Controladores Existentes
- Todos los controllers existentes - Agregar validación de autenticación

### Configuración
- `application.yaml` - Configuración JWT y seguridad

## 🧪 Casos de Prueba

### Autenticación
1. **Registro exitoso**: Usuario nuevo se registra correctamente
2. **Login exitoso**: Usuario existente hace login con credenciales válidas
3. **Login fallido**: Credenciales incorrectas devuelven error 401
4. **Token válido**: Request con JWT válido accede a endpoint protegido
5. **Token expirado**: Request con JWT expirado devuelve error 401
6. **Refresh token**: Renovación de token funciona correctamente
7. **Logout**: Tokens se invalidan correctamente

### Autorización
1. **Endpoint público**: /auth/login accesible sin token
2. **Endpoint protegido**: /api/users requiere token válido
3. **Propietario de tarea**: Usuario solo ve sus tareas asignadas
4. **Usuario no autorizado**: No puede acceder a tareas de otros usuarios

### Seguridad
1. **Rate limiting**: Múltiples intentos de login fallan apropiadamente
2. **Password hashing**: Contraseñas se almacenan hasheadas
3. **Token blacklist**: Tokens invalidados no permiten acceso
4. **CORS**: Headers apropiados para peticiones cross-origin

## ⚙️ Configuración

### application.yaml
```yaml
app:
  jwt:
    secret: "mySecretKey123456789012345678901234567890"
    access-token-expiration: 900000      # 15 minutes
    refresh-token-expiration: 2592000000 # 30 days
    issuer: "task-manager-api"

  security:
    rate-limit:
      login-attempts: 5
      lockout-duration: 900 # 15 minutes
    password:
      min-length: 8
      require-special-chars: true
      require-numbers: true
      require-uppercase: true

spring:
  security:
    user:
      name: admin
      password: admin123
      roles: ADMIN
```

### Ejemplo de Uso

#### Registro
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "password": "SecurePass123!"
  }'
```

#### Login
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "SecurePass123!"
  }'
```

#### Acceso a API Protegida
```bash
curl -X GET http://localhost:8080/api/users/me \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

## 🔗 Dependencias Adicionales

### Maven Dependencies
```xml
<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>

<!-- Password Encryption -->
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-crypto</artifactId>
</dependency>
```

## 🚀 Definición de Terminado (DoD)

- [ ] Sistema completo de autenticación JWT implementado
- [ ] Todos los endpoints protegidos apropiadamente
- [ ] Registro y login funcionando correctamente
- [ ] Refresh tokens implementados y funcionando
- [ ] Rate limiting para prevenir ataques
- [ ] Passwords hasheadas con BCrypt
- [ ] Tests de seguridad pasando (> 90% cobertura)
- [ ] Documentación de seguridad completa
- [ ] Configuración para diferentes entornos
- [ ] Manejo robusto de errores de autenticación
- [ ] Performance testing con tokens

## 📈 Métricas de Éxito

- Autenticación en < 200ms por request
- Cero vulnerabilidades de seguridad identificadas
- Rate limiting efectivo contra ataques de fuerza bruta
- 100% de endpoints críticos protegidos

---

**Estimación:** 8 Story Points
**Prioridad:** Alta (Bloqueante para producción)
**Sprint:** Sprint 2
**Dependencias:** Modificar todos los componentes existentes
**Asignado a:** Claude Code Workshop Team