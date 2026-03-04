# Task Manager API 📋

> **Claude Code Workshop Demo Application**

Una API REST completa para gestión de tareas desarrollada específicamente para demostrar el flujo de trabajo completo de **Claude Code**. Esta aplicación sirve como contexto base para el workshop, mostrando cómo Claude Code puede analizar, planificar, implementar y mejorar funcionalidades en aplicaciones Java Spring Boot reales.

## 🎯 Propósito del Proyecto

Este proyecto **NO** es una aplicación de producción, sino una **demostración educativa** que ilustra:

- ✅ **Funcionalidades base implementadas** - Para que Claude Code tenga contexto
- 🔄 **Funcionalidades pendientes** - Para demostrar el workflow completo
- 📚 **Documentación tipo Jira** - Stories realistas para el workshop
- 🧪 **Base de tests** - Estructura expandible durante demos
- 🛠️ **Arquitectura típica** - Patrones comunes en aplicaciones empresariales

## 🏗️ Arquitectura del Sistema

```
📦 Task Manager API
├── 👥 Gestión de Usuarios
├── 📋 Gestión de Tareas
├── 📁 Organización por Proyectos
├── 💬 Sistema de Comentarios
├── 🔍 Búsqueda Básica (expandible)
├── 📊 Base para Reportes
├── 🔐 Preparado para Autenticación
└── 📝 Base para Auditoría
```

## 🚀 Inicio Rápido

### Prerrequisitos
- ☕ **Java 17+**
- 📦 **Maven 3.6+**
- 🌐 **Puerto 8080 disponible**

### Instalación y Ejecución

1. **Clonar el repositorio**
   ```bash
   git clone <repository-url>
   cd task-manager-api
   ```

2. **Ejecutar la aplicación**
   ```bash
   mvn spring-boot:run
   ```

3. **Verificar funcionamiento**
   ```bash
   # Health check
   curl http://localhost:8080/actuator/health

   # Listar usuarios predefinidos
   curl http://localhost:8080/api/users

   # Listar tareas de ejemplo
   curl http://localhost:8080/api/tasks
   ```

### 🎮 Consola de Base de Datos (H2)

Accede a la consola web para explorar los datos:
- **URL**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:taskmanager`
- **Usuario**: `sa`
- **Contraseña**: `password`

## 📊 Datos de Ejemplo Incluidos

La aplicación se inicializa automáticamente con:

### 👥 Usuarios (3)
- **Alice Johnson** - alice.johnson@taskmanager.com
- **Bob Smith** - bob.smith@taskmanager.com
- **Charlie Davis** - charlie.davis@taskmanager.com

### 📁 Proyectos (2)
- **Website Redesign** - Redesign completo del sitio web
- **Mobile App Development** - App multiplataforma iOS/Android

### 📋 Tareas (8)
- Variedad de estados: TODO, IN_PROGRESS, DONE
- Diferentes asignaciones y fechas de vencimiento
- Tareas con y sin proyectos asociados

### 💬 Comentarios (7)
- Conversaciones realistas en las tareas
- Diferentes autores y timestamps

## 🛠️ API Endpoints Implementados

### 👥 Gestión de Usuarios
```http
GET    /api/users              # Listar todos los usuarios
POST   /api/users              # Crear nuevo usuario
GET    /api/users/{id}         # Obtener usuario por ID
PUT    /api/users/{id}         # Actualizar usuario
DELETE /api/users/{id}         # Eliminar usuario
GET    /api/users/search       # Buscar usuarios por nombre
```

### 📋 Gestión de Tareas
```http
GET    /api/tasks                    # Listar tareas (paginado)
POST   /api/tasks                    # Crear nueva tarea
GET    /api/tasks/{id}               # Obtener tarea por ID
PUT    /api/tasks/{id}               # Actualizar tarea
DELETE /api/tasks/{id}               # Eliminar tarea
GET    /api/tasks/status/{status}    # Filtrar por estado
GET    /api/tasks/assigned/{userId}  # Tareas asignadas a usuario
GET    /api/tasks/project/{projectId} # Tareas de un proyecto
GET    /api/tasks/overdue            # Tareas vencidas
GET    /api/tasks/search             # Búsqueda por título
PUT    /api/tasks/{id}/assign/{userId} # Asignar tarea
PUT    /api/tasks/{id}/status        # Cambiar estado
```

### 📁 Gestión de Proyectos
```http
GET    /api/projects              # Listar proyectos
POST   /api/projects              # Crear proyecto
GET    /api/projects/{id}         # Obtener proyecto
PUT    /api/projects/{id}         # Actualizar proyecto
DELETE /api/projects/{id}         # Eliminar proyecto
```

### 💬 Sistema de Comentarios
```http
GET    /api/comments                   # Listar comentarios
POST   /api/comments                   # Crear comentario
GET    /api/comments/task/{taskId}     # Comentarios de una tarea
PUT    /api/comments/{id}              # Actualizar comentario
DELETE /api/comments/{id}              # Eliminar comentario
```

### 🔍 Búsqueda Avanzada (Base)
```http
GET /api/tasks/advanced-search?status=TODO&assignedTo=1&createdAfter=2024-01-01
```

## 📚 Workshop Stories - Funcionalidades Pendientes

El directorio `src/main/resources/mock-jira-stories/` contiene **5 historias de usuario** detalladas que simulan tickets de Jira reales:

### 🔍 [TASK-001: Filtros Avanzados](src/main/resources/mock-jira-stories/TASK-001-filters.md)
- **Objetivo**: Implementar filtrado granular de tareas
- **Funcionalidades**: Múltiples criterios, rangos de fechas, combinaciones complejas
- **Estimación**: 5 Story Points

### 📧 [TASK-002: Notificaciones por Email](src/main/resources/mock-jira-stories/TASK-002-notifications.md)
- **Objetivo**: Sistema completo de notificaciones
- **Funcionalidades**: Emails automáticos, plantillas, configuración por usuario
- **Estimación**: 8 Story Points

### 📊 [TASK-003: Sistema de Reportes](src/main/resources/mock-jira-stories/TASK-003-reports.md)
- **Objetivo**: Dashboard y reportes ejecutivos
- **Funcionalidades**: Métricas, exportación PDF/Excel, programación automática
- **Estimación**: 13 Story Points

### 🔐 [TASK-004: Autenticación JWT](src/main/resources/mock-jira-stories/TASK-004-auth.md)
- **Objetivo**: Seguridad robusta con JWT
- **Funcionalidades**: Login/registro, protección de endpoints, refresh tokens
- **Estimación**: 8 Story Points

### 📝 [TASK-005: Auditoría de Cambios](src/main/resources/mock-jira-stories/TASK-005-audit.md)
- **Objetivo**: Trazabilidad completa de acciones
- **Funcionalidades**: Logs inmutables, dashboard de auditoría, compliance
- **Estimación**: 13 Story Points

## 🧪 Testing y Calidad

### Ejecutar Tests
```bash
# Tests unitarios
mvn test

# Tests con reporte de cobertura
mvn test jacoco:report

# Tests de integración
mvn verify
```

### Estructura de Tests
- **Unit Tests**: Servicios y lógica de negocio
- **Integration Tests**: Controladores y endpoints
- **Repository Tests**: Consultas JPA personalizadas
- **Security Tests**: (Pendiente para TASK-004)

### Cobertura Actual
- **Servicios**: ~85% cobertura
- **Controladores**: ~75% cobertura
- **Modelos**: 100% cobertura

## 🔧 Stack Tecnológico

### Backend
- **Java 17** - Lenguaje principal
- **Spring Boot 3.2** - Framework base
- **Spring Data JPA** - Persistencia
- **Spring Security** - Seguridad (preparado para TASK-004)
- **H2 Database** - Base de datos en memoria
- **Maven** - Gestión de dependencias

### Testing
- **JUnit 5** - Framework de testing
- **Mockito** - Mocking para unit tests
- **Spring Boot Test** - Integration testing
- **TestContainers** - (Preparado para tests de integración avanzados)

### Utilidades
- **Jackson** - Serialización JSON
- **Validation API** - Validación de datos
- **JavaMail** - (Preparado para TASK-002)
- **JWT Library** - (Preparado para TASK-004)

## 🌍 Configuración de Entornos

### Desarrollo (Default)
```yaml
# application.yaml
spring:
  profiles:
    active: dev
  h2:
    console:
      enabled: true
logging:
  level:
    com.workshop.taskmanager: DEBUG
```

### Testing
```yaml
# application-test.yaml
spring:
  jpa:
    show-sql: false
  sql:
    init:
      mode: never
```

### Producción (Simulada)
```yaml
# application-prod.yaml
spring:
  h2:
    console:
      enabled: false
logging:
  level:
    root: WARN
```

## 📈 Métricas de Desarrollo

### Performance Actual
- ⚡ **Startup time**: ~8 segundos
- 🚀 **Response time**: <200ms (endpoints básicos)
- 💾 **Memory usage**: ~150MB (con H2)
- 📊 **Throughput**: 500+ requests/sec

### Objetivos Post-Workshop
- 🔍 **Búsqueda avanzada**: <500ms (TASK-001)
- 📧 **Envío de emails**: Procesamiento asíncrono (TASK-002)
- 📊 **Generación de reportes**: <2s para dashboards (TASK-003)
- 🔐 **Autenticación**: <200ms por login (TASK-004)
- 📝 **Auditoría**: Sin impacto en performance principal (TASK-005)

## 🎓 Uso en Workshop de Claude Code

### Flujo Típico de Demostración

1. **📋 Análisis inicial**
   ```
   "Implementa filtros avanzados según TASK-001"
   ```

2. **🔍 Claude analiza**
   - Lee la story en `mock-jira-stories/TASK-001-filters.md`
   - Examina código existente (TaskController, TaskService, etc.)
   - Identifica patrones y arquitectura actual

3. **🧠 Planificación**
   - Propone implementación específica
   - Identifica archivos a modificar/crear
   - Sugiere estrategia de testing

4. **⚒️ Implementación**
   - Crea `TaskFilterDto.java`
   - Modifica `TaskService.java` con nuevos métodos
   - Actualiza `TaskController.java` con endpoints mejorados
   - Agrega tests correspondientes

5. **✅ Validación**
   - Ejecuta tests existentes y nuevos
   - Verifica que no hay regresiones
   - Confirma funcionalidad según criterios de aceptación

### Comandos de Ejemplo para Workshop

```bash
# Comando típico de Claude Code
"Implementa los filtros avanzados de tareas según la especificación en TASK-001"

# Comando de seguimiento
"Agrega tests de integración para los nuevos filtros"

# Comando de mejora
"Optimiza las queries de filtrado para mejor performance"
```

## 🚨 Limitaciones Conocidas

### Por Diseño (Para el Workshop)
- 🔓 **Sin autenticación** - Implementar en TASK-004
- 📧 **Sin notificaciones** - Implementar en TASK-002
- 📊 **Reportes básicos** - Expandir en TASK-003
- 🔍 **Filtros limitados** - Mejorar en TASK-001
- 📝 **Sin auditoría** - Implementar en TASK-005

### Técnicas
- 💾 **Base de datos en memoria** - Se resetea en cada inicio
- 🚫 **Sin validación robusta** - Mejorará con cada story
- ⚡ **Sin optimización de queries** - Mejorará con el workshop
- 🔒 **Sin CORS configurado** - Agregar según necesidad

## 📞 Soporte y Contacto

### Para el Workshop
- 📧 **Instructor**: Claude Code Workshop Team
- 📚 **Documentación**: Este README + stories en `/mock-jira-stories/`
- 🐛 **Issues**: Usar como ejemplos de debugging con Claude Code

### Recursos Adicionales
- 🌐 **H2 Console**: http://localhost:8080/h2-console
- 📊 **Actuator Health**: http://localhost:8080/actuator/health
- 📋 **API Docs**: (Expandir con Swagger en workshop)

## 📄 Licencia

Este proyecto es únicamente para **propósitos educativos** del Claude Code Workshop.

---

## 🎉 ¡Listo para el Workshop!

Esta aplicación está preparada para demostrar el poder de **Claude Code** en el desarrollo de software. Cada historia pendiente representa un escenario realista donde Claude Code puede:

- 🔍 **Analizar** código existente y requisitos
- 🧠 **Planificar** implementaciones robustas
- ⚒️ **Implementar** funcionalidades completas
- 🧪 **Probar** y validar el código
- 📚 **Documentar** los cambios realizados

**¡Comienza el workshop y experimenta el futuro del desarrollo de software con Claude Code!** 🚀