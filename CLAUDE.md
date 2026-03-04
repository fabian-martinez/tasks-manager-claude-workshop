# Claude Code Configuration - Task Manager API Workshop

> **Implementación del Claude Code Work Flow según diagrama de proceso**

## 🎯 Contexto del Proyecto

Este es el **Task Manager API**, una aplicación Java Spring Boot diseñada específicamente para demostrar el flujo completo de trabajo de Claude Code. La aplicación sirve como contexto base para workshops y entrenamientos.

### Información del Proyecto
- **Nombre**: Task Manager API
- **Tipo**: Java Spring Boot Application
- **Propósito**: Demostración de Claude Code Workflow
- **Stack**: Java 17, Spring Boot 3.2, H2 Database, Maven
- **Estado**: Base funcional implementada, 5 stories pendientes para workshop

## 🔄 Claude Code Workflow Implementation

### Fase 1: Inicialización del Contexto (Orchestrator)
Cuando recibas un prompt del desarrollador:

1. **Analizar el contexto actual** del proyecto
2. **Identificar el tipo de tarea**:
   - Nueva funcionalidad (Stories TASK-001 a TASK-005)
   - Bug fix en código existente
   - Refactoring o mejora
   - Análisis de código

3. **Activar análisis paralelo** según el tipo de tarea

### Fase 2: Sub Agents Pool (Análisis Paralelo)

#### Agente de Análisis de Jira
**Activación**: Cuando se mencione TASK-001, TASK-002, TASK-003, TASK-004, o TASK-005
- Leer el archivo correspondiente en `src/main/resources/mock-jira-stories/`
- Extraer criterios de aceptación
- Identificar dependencias técnicas
- Generar plan de implementación

#### Agente de Análisis de Código
**Activación**: Para cualquier modificación de código
- Analizar impacto en arquitectura existente
- Identificar archivos a modificar/crear
- Revisar patrones existentes
- Validar consistencia con codebase

### Fase 3: Skills Engine (Brainstorming e Implementación)

#### Brainstorming Estructurado
Antes de implementar, siempre:
1. **Proponer arquitectura** detallada
2. **Listar archivos** a crear/modificar
3. **Identificar riesgos** y dependencias
4. **Solicitar aprobación** del plan

#### Implementación Secuencial
1. **Crear/modificar modelos** (si aplica)
2. **Implementar repositories** (si aplica)
3. **Desarrollar services** con lógica de negocio
4. **Crear/actualizar controllers**
5. **Implementar tests** unitarios e integración

### Fase 4: Hooks System (Automatización)

#### Pre-Implementation Hooks
- Verificar que el código compila: `mvn clean compile`
- Validar tests existentes no se rompan: `mvn test -Dtest="*ServiceTest"`

#### Post-Implementation Hooks
- Ejecutar tests completos: `mvn test`
- Verificar aplicación inicia: `timeout 10s mvn spring-boot:run`
- Generar documentación automática de cambios

## 📋 Guidelines de Desarrollo

### Estándares de Código
- **Java 17** syntax y features
- **Spring Boot** best practices
- **JPA/Hibernate** para persistencia
- **Jakarta Validation** para validaciones
- **Arquitectura en capas**: Controller → Service → Repository → Model

### Patrones a Seguir
- **DTOs** para transferencia de datos complejos
- **Service layer** transaccional con `@Transactional`
- **Exception handling** con responses HTTP apropiados
- **Repository pattern** con Spring Data JPA
- **Builder pattern** para objetos complejos (si aplica)

### Testing Strategy
- **Unit tests** para services (obligatorio)
- **Integration tests** para controllers (recomendado)
- **Repository tests** para queries complejas
- **Mock external dependencies** en tests

### Naming Conventions
- **Classes**: PascalCase (ej: `TaskService`, `UserController`)
- **Methods**: camelCase (ej: `findTasksByStatus`, `createNewUser`)
- **Variables**: camelCase (ej: `taskRepository`, `currentUser`)
- **Constants**: UPPER_SNAKE_CASE (ej: `DEFAULT_PAGE_SIZE`)
- **Packages**: lowercase (ej: `com.workshop.taskmanager.service`)

## 🎯 Stories Disponibles para Workshop

### TASK-001: Filtros Avanzados (5 SP)
**Archivo**: `src/main/resources/mock-jira-stories/TASK-001-filters.md`
**Objetivo**: Implementar filtrado granular de tareas
**Archivos clave**: `TaskFilterDto`, `TaskController`, `TaskService`

### TASK-002: Sistema de Notificaciones (8 SP)
**Archivo**: `src/main/resources/mock-jira-stories/TASK-002-notifications.md`
**Objetivo**: Emails automáticos para eventos de tareas
**Archivos clave**: `NotificationService`, `EmailService`, `NotificationSettings`

### TASK-003: Reportes y Dashboard (13 SP)
**Archivo**: `src/main/resources/mock-jira-stories/TASK-003-reports.md`
**Objetivo**: Métricas y reportes ejecutivos
**Archivos clave**: `ReportService`, `DashboardController`, `ExportService`

### TASK-004: Autenticación JWT (8 SP)
**Archivo**: `src/main/resources/mock-jira-stories/TASK-004-auth.md`
**Objetivo**: Seguridad completa con JWT
**Archivos clave**: `AuthController`, `JwtService`, `SecurityConfig`

### TASK-005: Auditoría (13 SP)
**Archivo**: `src/main/resources/mock-jira-stories/TASK-005-audit.md`
**Objetivo**: Trazabilidad completa de cambios
**Archivos clave**: `AuditService`, `AuditLog`, `AuditAspect`

## 🛠️ Comandos y Verificaciones

### Compilación y Tests
```bash
# Compilar proyecto
mvn clean compile

# Ejecutar tests de servicio (rápidos)
mvn test -Dtest="*ServiceTest"

# Ejecutar todos los tests
mvn test

# Iniciar aplicación
mvn spring-boot:run
```

### Endpoints para Verificación
```bash
# Health check
curl http://localhost:8080/actuator/health

# APIs base funcionales
curl http://localhost:8080/api/users
curl http://localhost:8080/api/tasks
curl http://localhost:8080/api/projects
curl http://localhost:8080/api/comments

# H2 Database Console
# URL: http://localhost:8080/h2-console
# JDBC: jdbc:h2:mem:taskmanager
# User: sa, Password: password
```

## 🔧 Configuración de Desarrollo

### Base de Datos
- **Tipo**: H2 in-memory (desarrollo)
- **Datos iniciales**: `src/main/resources/data.sql`
- **3 usuarios**, **2 proyectos**, **8 tareas**, **7 comentarios**

### Profiles Disponibles
- **default/dev**: Desarrollo con H2 console habilitado
- **test**: Testing sin datos iniciales
- **prod**: Producción con logs mínimos

### IDE Recomendaciones
- **IntelliJ IDEA** o **VS Code** con Extension Pack for Java
- **Maven integration** habilitado
- **Spring Boot tools** instalados

## ⚡ Workflow Automation Hooks

### Trigger Commands
Los siguientes comandos activan automatización específica:

- `"Implementar TASK-XXX"` → Activa análisis de Jira + plan de implementación
- `"Analizar impacto de..."` → Activa análisis profundo de código
- `"Refactorizar..."` → Activa análisis de arquitectura + plan de refactoring
- `"Agregar tests para..."` → Activa generación de tests específicos

### Auto-Validations
Claude Code ejecutará automáticamente:
- Compilación antes de mostrar código
- Verificación de tests existentes
- Validación de sintaxis y imports
- Verificación de patterns del proyecto

## 📚 Recursos y Referencias

### Documentación del Proyecto
- **README.md**: Documentación completa del proyecto
- **WORKSHOP-GUIDE.md**: Guía para facilitadores de workshop
- **Mock Jira Stories**: Especificaciones detalladas en `/mock-jira-stories/`

### Standards y Convenciones
- **Spring Boot Reference**: https://docs.spring.io/spring-boot/docs/current/reference/html/
- **Spring Data JPA**: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
- **Jakarta Validation**: https://beanvalidation.org/3.0/spec/

---

## 🎉 Workshop Flow

### Comando Típico de Inicio
```
"Implementar los filtros avanzados según TASK-001"
```

### Flujo Esperado
1. **Claude analiza** TASK-001-filters.md
2. **Propone plan** de implementación detallado
3. **Solicita aprobación** del desarrollador
4. **Implementa** secuencialmente: DTO → Service → Controller → Tests
5. **Verifica** que todo compile y tests pasen
6. **Documenta** cambios realizados

### Métricas de Éxito
- ✅ **Funcionalidad completa** según criterios de aceptación
- ✅ **Tests pasando** sin regresiones
- ✅ **Código consistente** con patrones existentes
- ✅ **Documentación** actualizada automáticamente

**¡Este proyecto está optimizado para demostrar el máximo potencial de Claude Code!** 🚀