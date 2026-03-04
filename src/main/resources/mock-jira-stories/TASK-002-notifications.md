# TASK-002: Sistema de Notificaciones por Email

## 📋 User Story

**Como** usuario del sistema Task Manager
**Quiero** recibir notificaciones por email cuando ocurran eventos importantes
**Para** mantenerme informado sobre cambios en mis tareas sin necesidad de revisar constantemente la aplicación

## 🎯 Objetivos de Negocio

- Mejorar la comunicación y colaboración entre miembros del equipo
- Reducir la pérdida de información importante sobre cambios en tareas
- Aumentar la responsabilidad y seguimiento de asignaciones
- Proporcionar trazabilidad de eventos importantes del sistema

## 📝 Descripción Detallada

El sistema actualmente no tiene capacidades de notificación, lo que significa que los usuarios deben verificar manualmente las actualizaciones. Se necesita implementar un sistema de notificaciones por email que se active automáticamente cuando ocurran eventos específicos relacionados con tareas.

## ✅ Criterios de Aceptación

### Eventos de Notificación
- [ ] **Asignación de tarea**
  - Enviar email cuando se asigne una tarea a un usuario
  - Incluir detalles de la tarea (título, descripción, fecha de vencimiento)
  - Incluir enlace directo a la tarea

- [ ] **Cambio de estado**
  - Notificar al creador y asignado cuando cambie el estado de una tarea
  - Incluir estado anterior y nuevo estado
  - Notificar especialmente cuando una tarea se marca como completada

- [ ] **Tareas próximas a vencer**
  - Enviar recordatorio 2 días antes de la fecha de vencimiento
  - Enviar recordatorio el día de vencimiento
  - Solo para tareas en estado TODO o IN_PROGRESS

- [ ] **Comentarios nuevos**
  - Notificar al asignado y creador cuando se agregue un comentario
  - Incluir contenido del comentario y autor
  - Excluir al autor del comentario de las notificaciones

### Configuración de Notificaciones
- [ ] **Preferencias por usuario**
  - Permitir habilitar/deshabilitar notificaciones por tipo
  - Configuración de horarios (no enviar en horarios nocturnos)
  - Frecuencia de resúmenes (diario, semanal)

- [ ] **Plantillas de email**
  - Templates HTML profesionales y responsive
  - Personalización con datos dinámicos
  - Consistencia visual con la marca de la empresa

### Funcionalidad Técnica
- [ ] **Cola de emails**
  - Procesamiento asíncrono de notificaciones
  - Reintentos automáticos en caso de falla
  - Log de notificaciones enviadas y errores

- [ ] **Configuración flexible**
  - Configuración via application.yaml
  - Soporte para diferentes proveedores de email (SMTP, SendGrid, etc.)
  - Configuración de desarrollo con email local (MailDev)

## 🔧 Especificación Técnica

### Nuevos Endpoints

#### Configuración de Notificaciones del Usuario
```
GET /api/users/{userId}/notification-settings
PUT /api/users/{userId}/notification-settings
```

#### Historial de Notificaciones
```
GET /api/notifications/user/{userId}
GET /api/notifications/task/{taskId}
```

### Modelos de Datos

#### NotificationSettings
```java
@Entity
public class NotificationSettings {
    private Long userId;
    private boolean taskAssigned;
    private boolean statusChanged;
    private boolean dueDateReminders;
    private boolean newComments;
    private String quietHoursStart; // "22:00"
    private String quietHoursEnd;   // "08:00"
    private String timezone;        // "America/New_York"
}
```

#### Notification
```java
@Entity
public class Notification {
    private Long id;
    private NotificationType type;
    private Long userId;
    private Long taskId;
    private String subject;
    private String content;
    private boolean sent;
    private LocalDateTime sentAt;
    private String errorMessage;
}
```

### Tipos de Notificación
```java
public enum NotificationType {
    TASK_ASSIGNED,
    STATUS_CHANGED,
    DUE_DATE_REMINDER,
    NEW_COMMENT,
    TASK_OVERDUE
}
```

### Plantillas de Email

#### Tarea Asignada
```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Nueva Tarea Asignada</title>
</head>
<body>
    <h2>Se te ha asignado una nueva tarea</h2>
    <div class="task-details">
        <h3>{{taskTitle}}</h3>
        <p><strong>Descripción:</strong> {{taskDescription}}</p>
        <p><strong>Proyecto:</strong> {{projectName}}</p>
        <p><strong>Fecha de vencimiento:</strong> {{dueDate}}</p>
        <p><strong>Asignado por:</strong> {{assignedByName}}</p>
    </div>
    <a href="{{taskLink}}" class="btn">Ver Tarea</a>
</body>
</html>
```

## 🗂️ Archivos a Crear

### Servicios y Componentes
- `src/main/java/com/workshop/taskmanager/service/NotificationService.java`
- `src/main/java/com/workshop/taskmanager/service/EmailService.java`
- `src/main/java/com/workshop/taskmanager/service/EmailTemplateService.java`

### Modelos
- `src/main/java/com/workshop/taskmanager/model/Notification.java`
- `src/main/java/com/workshop/taskmanager/model/NotificationSettings.java`
- `src/main/java/com/workshop/taskmanager/model/NotificationType.java`

### DTOs
- `src/main/java/com/workshop/taskmanager/dto/NotificationDto.java`
- `src/main/java/com/workshop/taskmanager/dto/NotificationSettingsDto.java`

### Repositorios
- `src/main/java/com/workshop/taskmanager/repository/NotificationRepository.java`
- `src/main/java/com/workshop/taskmanager/repository/NotificationSettingsRepository.java`

### Controladores
- `src/main/java/com/workshop/taskmanager/controller/NotificationController.java`

### Configuración
- `src/main/java/com/workshop/taskmanager/config/EmailConfig.java`

### Plantillas
- `src/main/resources/email-templates/task-assigned.html`
- `src/main/resources/email-templates/status-changed.html`
- `src/main/resources/email-templates/due-date-reminder.html`
- `src/main/resources/email-templates/new-comment.html`

### Tests
- `src/test/java/com/workshop/taskmanager/service/NotificationServiceTest.java`
- `src/test/java/com/workshop/taskmanager/service/EmailServiceTest.java`

## 🗄️ Archivos a Modificar

### Servicios Existentes
- `TaskService.java` - Agregar eventos de notificación
- `CommentService.java` - Notificar sobre nuevos comentarios

### Configuración
- `application.yaml` - Configuración de email y notificaciones

## 🧪 Casos de Prueba

### Funcionalidad de Notificaciones
1. **Asignación**: Verificar email enviado cuando se asigna tarea
2. **Cambio de estado**: Email enviado cuando cambia estado TODO → IN_PROGRESS
3. **Recordatorio**: Email enviado 2 días antes de vencimiento
4. **Comentario**: Email enviado cuando se agrega comentario (excepto autor)
5. **Configuración**: Respetar preferencias de usuario (notificaciones deshabilitadas)

### Casos de Error
1. **SMTP no disponible**: Manejar error y reintentar
2. **Email inválido**: Log error y continuar procesamiento
3. **Template no encontrado**: Usar template por defecto

### Performance
1. **Volumen alto**: Procesar 1000 notificaciones en < 10 segundos
2. **Asíncrono**: No bloquear operaciones principales

## ⚙️ Configuración Requerida

### application.yaml
```yaml
app:
  notification:
    enabled: true
    email:
      from: "noreply@taskmanager.com"
      fromName: "Task Manager"
      replyTo: "support@taskmanager.com"
    templates:
      base-url: "http://localhost:8080"
    retry:
      max-attempts: 3
      delay: 30000 # 30 seconds
    batch-size: 50

spring:
  mail:
    host: localhost
    port: 1025
    properties:
      mail:
        smtp:
          auth: false
```

## 🔗 Dependencias Adicionales

### Maven Dependencies
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context-support</artifactId>
</dependency>
<dependency>
    <groupId>org.freemarker</groupId>
    <artifactId>freemarker</artifactId>
</dependency>
```

## 🚀 Definición de Terminado (DoD)

- [ ] Sistema de notificaciones implementado y funcionando
- [ ] Todos los tipos de eventos generan notificaciones apropiadas
- [ ] Plantillas de email responsive y profesionales
- [ ] Configuración de usuario funcional
- [ ] Procesamiento asíncrono implementado
- [ ] Tests unitarios e integración > 85% cobertura
- [ ] Configuración para desarrollo y producción
- [ ] Documentación de configuración completa
- [ ] Manejo de errores robusto con logs apropiados

## 📈 Métricas de Éxito

- Tasa de entrega de emails > 98%
- Tiempo de procesamiento por notificación < 100ms
- Cero impacto en performance de operaciones principales
- Satisfacción del usuario con notificaciones útiles

---

**Estimación:** 8 Story Points
**Prioridad:** Media-Alta
**Sprint:** Sprint 2
**Dependencias:** TASK-001 (opcional, para notificaciones de filtros)
**Asignado a:** Claude Code Workshop Team