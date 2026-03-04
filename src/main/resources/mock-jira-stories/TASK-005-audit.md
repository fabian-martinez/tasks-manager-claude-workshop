# TASK-005: Sistema de Auditoría de Cambios

## 📋 User Story

**Como** administrador del sistema y auditor de compliance
**Quiero** tener un registro completo de todos los cambios realizados en el sistema
**Para** cumplir con requisitos de auditoría, investigar problemas, y mantener trazabilidad de acciones

## 🎯 Objetivos de Negocio

- Cumplir con requisitos regulatorios y de compliance (SOX, GDPR, etc.)
- Proporcionar trazabilidad completa de cambios para auditorías
- Facilitar debugging e investigación de problemas
- Detectar patrones de uso y comportamientos anómalos
- Establecer responsabilidad y accountability en acciones del sistema

## 📝 Descripción Detallada

El sistema actual no mantiene registro de cambios, lo que dificulta la auditoría, debugging, y cumplimiento regulatorio. Se necesita implementar un sistema completo de auditoría que capture automáticamente todos los cambios significativos en entidades principales (usuarios, tareas, proyectos, comentarios) junto con metadata contextual.

## ✅ Criterios de Aceptación

### Captura Automática de Cambios
- [ ] **Eventos de auditoría**
  - Crear, actualizar, eliminar para todas las entidades principales
  - Login/logout de usuarios y cambios de autenticación
  - Cambios de estado de tareas con contexto adicional
  - Acceso a endpoints sensibles (reportes, datos de otros usuarios)

- [ ] **Metadata completa**
  - Usuario que realizó la acción (basado en JWT)
  - Timestamp exacto con zona horaria
  - Dirección IP y User-Agent del request
  - Valores anteriores y nuevos (before/after)
  - Contexto adicional (ID de proyecto, tarea relacionada, etc.)

### Almacenamiento de Logs de Auditoría
- [ ] **Tabla de auditoría centralizada**
  - Estructura flexible para diferentes tipos de eventos
  - Campos estandarizados con JSON para datos específicos
  - Particionado por fecha para performance
  - Retención configurable de datos históricos

- [ ] **Integridad de logs**
  - Logs de auditoría inmutables (solo inserción)
  - Validación de integridad con checksums
  - Backup automático de logs críticos
  - Protección contra manipulación no autorizada

### Consulta y Reportes de Auditoría
- [ ] **API de consulta de auditoría**
  - Búsqueda por usuario, entidad, rango de fechas
  - Filtros por tipo de acción y nivel de severidad
  - Paginación eficiente para grandes volúmenes
  - Exportación de logs para herramientas externas

- [ ] **Dashboard de auditoría**
  - Resumen de actividad por usuario y período
  - Alertas de actividad sospechosa
  - Métricas de uso por funcionalidad
  - Trends de actividad del sistema

## 🔧 Especificación Técnica

### Modelo de Auditoría

#### AuditLog Entity
```java
@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    private String id; // UUID

    @Column(nullable = false)
    private String entityType; // "USER", "TASK", "PROJECT", "COMMENT"

    @Column(nullable = false)
    private String entityId; // ID de la entidad afectada

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditAction action; // CREATE, UPDATE, DELETE, LOGIN, LOGOUT, ACCESS

    @Column(name = "user_id")
    private Long userId; // Usuario que realizó la acción

    @Column(name = "user_email")
    private String userEmail; // Email para referencia

    @Column(name = "session_id")
    private String sessionId; // ID de sesión

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "old_values", columnDefinition = "TEXT")
    private String oldValues; // JSON con valores anteriores

    @Column(name = "new_values", columnDefinition = "TEXT")
    private String newValues; // JSON con valores nuevos

    @Column(name = "additional_info", columnDefinition = "TEXT")
    private String additionalInfo; // JSON con contexto adicional

    @Enumerated(EnumType.STRING)
    private AuditLevel level; // INFO, WARN, ERROR, CRITICAL

    @Column(name = "checksum")
    private String checksum; // Para validación de integridad
}
```

#### Enums
```java
public enum AuditAction {
    CREATE, UPDATE, DELETE,
    LOGIN, LOGOUT, LOGIN_FAILED,
    ACCESS, EXPORT, REPORT_GENERATED,
    PASSWORD_CHANGED, EMAIL_CHANGED,
    TASK_STATUS_CHANGED, TASK_ASSIGNED
}

public enum AuditLevel {
    INFO,     // Operaciones normales
    WARN,     // Operaciones que requieren atención
    ERROR,    // Errores o operaciones fallidas
    CRITICAL  // Eventos de alta seguridad
}
```

### Nuevos Endpoints

#### Consulta de Auditoría
```
GET  /api/audit/logs              - Listar logs con filtros
GET  /api/audit/logs/{id}         - Detalle de log específico
GET  /api/audit/entity/{type}/{id} - Logs de una entidad específica
GET  /api/audit/user/{userId}     - Logs de un usuario
GET  /api/audit/export            - Exportar logs (CSV/JSON)
GET  /api/audit/dashboard         - Dashboard de métricas
```

#### Administración (solo admins)
```
DELETE /api/audit/cleanup         - Limpiar logs antiguos
POST   /api/audit/integrity-check - Verificar integridad de logs
```

### Interceptor de Auditoría

#### AuditAspect
```java
@Aspect
@Component
public class AuditAspect {

    @Around("@annotation(Auditable)")
    public Object auditMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        // Capturar estado anterior
        Object oldState = captureCurrentState(joinPoint);

        try {
            // Ejecutar método
            Object result = joinPoint.proceed();

            // Capturar estado nuevo y crear log
            Object newState = captureNewState(result, joinPoint);
            createAuditLog(AuditAction.UPDATE, oldState, newState, joinPoint);

            return result;
        } catch (Exception e) {
            // Log de error
            createErrorAuditLog(e, joinPoint);
            throw e;
        }
    }
}
```

#### Annotation para Auditoría
```java
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {
    AuditAction action() default AuditAction.UPDATE;
    String entityType() default "";
    AuditLevel level() default AuditLevel.INFO;
}
```

## 🗂️ Archivos a Crear

### Modelos
- `src/main/java/com/workshop/taskmanager/model/AuditLog.java`
- `src/main/java/com/workshop/taskmanager/model/AuditAction.java`
- `src/main/java/com/workshop/taskmanager/model/AuditLevel.java`

### Servicios
- `src/main/java/com/workshop/taskmanager/service/AuditService.java`
- `src/main/java/com/workshop/taskmanager/service/AuditLogService.java`
- `src/main/java/com/workshop/taskmanager/service/AuditIntegrityService.java`

### Aspectos y Interceptores
- `src/main/java/com/workshop/taskmanager/audit/AuditAspect.java`
- `src/main/java/com/workshop/taskmanager/audit/Auditable.java`
- `src/main/java/com/workshop/taskmanager/audit/AuditContext.java`
- `src/main/java/com/workshop/taskmanager/audit/AuditEventPublisher.java`

### DTOs
- `src/main/java/com/workshop/taskmanager/dto/AuditLogDto.java`
- `src/main/java/com/workshop/taskmanager/dto/AuditSearchDto.java`
- `src/main/java/com/workshop/taskmanager/dto/AuditDashboardDto.java`

### Controladores
- `src/main/java/com/workshop/taskmanager/controller/AuditController.java`

### Repositorios
- `src/main/java/com/workshop/taskmanager/repository/AuditLogRepository.java`

### Utilidades
- `src/main/java/com/workshop/taskmanager/util/AuditUtils.java`
- `src/main/java/com/workshop/taskmanager/util/JsonDiffUtils.java`

### Configuración
- `src/main/java/com/workshop/taskmanager/config/AuditConfig.java`

### Tests
- `src/test/java/com/workshop/taskmanager/audit/AuditAspectTest.java`
- `src/test/java/com/workshop/taskmanager/service/AuditServiceTest.java`
- `src/test/java/com/workshop/taskmanager/controller/AuditControllerTest.java`

## 🗄️ Archivos a Modificar

### Servicios Existentes (Agregar @Auditable)
- `UserService.java` - Auditar creación/actualización/eliminación de usuarios
- `TaskService.java` - Auditar cambios de estado y asignaciones
- `ProjectService.java` - Auditar cambios en proyectos
- `CommentService.java` - Auditar comentarios
- `AuthService.java` - Auditar login/logout

### Controladores (Auditoría de acceso)
- Todos los controllers existentes para auditar acceso a endpoints

## 🧪 Casos de Prueba

### Funcionalidad de Auditoría
1. **Creación de entidad**: Log creado correctamente al crear tarea
2. **Actualización**: Captura valores before/after en actualización
3. **Eliminación**: Log con información de entidad eliminada
4. **Login/Logout**: Eventos de autenticación registrados
5. **Acceso a datos**: Logs de consultas a endpoints sensibles

### Integridad y Seguridad
1. **Inmutabilidad**: Logs no pueden ser modificados después de creación
2. **Checksum**: Validación de integridad funciona correctamente
3. **Contexto completo**: IP, User-Agent, timestamp capturados
4. **Filtrado de datos sensibles**: Passwords no aparecen en logs

### Performance y Volumen
1. **Alto volumen**: 10,000 logs creados sin impacto significativo
2. **Consultas eficientes**: Búsqueda en logs de 1M+ registros < 3s
3. **Async processing**: Auditoría no bloquea operaciones principales
4. **Particionado**: Consultas por rango de fechas optimizadas

### Consulta y Reportes
1. **Filtros múltiples**: Búsqueda por usuario + fecha + acción
2. **Exportación**: CSV/JSON generados correctamente
3. **Dashboard**: Métricas calculadas correctamente
4. **Paginación**: Manejo eficiente de resultados grandes

## 📊 Ejemplos de Logs

### Log de Creación de Tarea
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "entityType": "TASK",
  "entityId": "123",
  "action": "CREATE",
  "userId": 42,
  "userEmail": "john.doe@example.com",
  "sessionId": "sess_abc123",
  "ipAddress": "192.168.1.100",
  "userAgent": "Mozilla/5.0...",
  "timestamp": "2024-03-07T10:30:00Z",
  "oldValues": null,
  "newValues": {
    "title": "New Task",
    "description": "Task description",
    "status": "TODO",
    "assignedTo": null,
    "projectId": 5
  },
  "additionalInfo": {
    "endpoint": "/api/tasks",
    "method": "POST",
    "projectName": "Website Redesign"
  },
  "level": "INFO",
  "checksum": "a1b2c3d4e5f6..."
}
```

### Log de Cambio de Estado
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440001",
  "entityType": "TASK",
  "entityId": "123",
  "action": "TASK_STATUS_CHANGED",
  "userId": 42,
  "userEmail": "john.doe@example.com",
  "timestamp": "2024-03-07T14:15:00Z",
  "oldValues": {
    "status": "TODO"
  },
  "newValues": {
    "status": "IN_PROGRESS"
  },
  "additionalInfo": {
    "taskTitle": "New Task",
    "assignedTo": "jane.smith@example.com",
    "statusChangeReason": "Started working on implementation"
  },
  "level": "INFO"
}
```

## ⚙️ Configuración

### application.yaml
```yaml
app:
  audit:
    enabled: true
    async-processing: true
    thread-pool-size: 5

    retention:
      days: 365              # Mantener logs por 1 año
      cleanup-cron: "0 0 2 * * *" # Limpieza diaria a las 2 AM

    integrity:
      checksum-algorithm: "SHA-256"
      validation-enabled: true

    sensitive-fields:
      - "password"
      - "token"
      - "ssn"
      - "creditCard"

    levels:
      default: INFO
      authentication: WARN
      data-access: INFO
      admin-actions: CRITICAL

    export:
      max-records: 100000
      temp-directory: "/tmp/audit-exports"
```

## 🔗 Dependencias Adicionales

### Maven Dependencies
```xml
<!-- Para procesamiento JSON -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>

<!-- Para diff de objetos -->
<dependency>
    <groupId>de.danielbechler</groupId>
    <artifactId>java-object-diff</artifactId>
    <version>0.95</version>
</dependency>

<!-- Para generación de checksums -->
<dependency>
    <groupId>commons-codec</groupId>
    <artifactId>commons-codec</artifactId>
</dependency>

<!-- Para procesamiento asíncrono -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-async</artifactId>
</dependency>
```

## 🚀 Definición de Terminado (DoD)

- [ ] Sistema completo de auditoría implementado y funcionando
- [ ] Todos los eventos críticos capturados automáticamente
- [ ] Logs inmutables con validación de integridad
- [ ] API de consulta eficiente con filtros apropiados
- [ ] Dashboard de auditoría con métricas útiles
- [ ] Procesamiento asíncrono sin impacto en performance
- [ ] Retención y limpieza automática de datos antiguos
- [ ] Tests completos incluyendo casos de alto volumen
- [ ] Documentación de compliance y procedimientos
- [ ] Configuración flexible para diferentes entornos
- [ ] Exportación de datos para herramientas externas

## 📈 Métricas de Éxito

- Captura 100% de eventos configurados sin pérdidas
- Impacto < 5% en performance de operaciones principales
- Consultas de auditoría responden en < 3 segundos
- Cero fallos en validación de integridad
- Cumplimiento con requisitos regulatorios aplicables

---

**Estimación:** 13 Story Points
**Prioridad:** Media (Crítica para compliance)
**Sprint:** Sprint 4
**Dependencias:** TASK-004 (autenticación para contexto de usuario)
**Asignado a:** Claude Code Workshop Team