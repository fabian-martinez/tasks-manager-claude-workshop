# TASK-001: Implementar Filtros Avanzados para Tareas

## 📋 User Story

**Como** usuario de la API Task Manager
**Quiero** poder filtrar tareas utilizando múltiples criterios de búsqueda
**Para** encontrar rápidamente las tareas específicas que necesito gestionar

## 🎯 Objetivos de Negocio

- Mejorar la experiencia del usuario al buscar tareas específicas
- Reducir el tiempo necesario para encontrar información relevante
- Aumentar la productividad de los equipos de trabajo
- Preparar la base para futuras funcionalidades de reportes

## 📝 Descripción Detallada

Actualmente, la API tiene funcionalidad básica de búsqueda por título y filtros simples por estado o usuario. Los usuarios necesitan capacidades de filtrado más granulares para gestionar eficientemente grandes volúmenes de tareas.

La implementación debe extender el endpoint existente `/api/tasks/advanced-search` para soportar filtros adicionales y combinaciones complejas.

## ✅ Criterios de Aceptación

### Funcionalidad Principal
- [ ] **Filtro por rango de fechas**
  - Filtrar por fecha de creación (createdAfter, createdBefore)
  - Filtrar por fecha de vencimiento (dueAfter, dueBefore)
  - Soportar formato de fecha ISO 8601 (YYYY-MM-DD)

- [ ] **Filtro por estado múltiple**
  - Permitir selección de múltiples estados: TODO, IN_PROGRESS, DONE
  - Usar parámetro de consulta como array: `?status=TODO&status=IN_PROGRESS`

- [ ] **Filtro por usuario asignado**
  - Filtrar por ID del usuario asignado
  - Incluir opción para tareas sin asignar (assignedTo=null)

- [ ] **Filtro por proyecto**
  - Filtrar tareas por ID de proyecto
  - Incluir opción para tareas sin proyecto

- [ ] **Filtro por texto completo**
  - Búsqueda en título y descripción de la tarea
  - Búsqueda case-insensitive
  - Soporte para búsqueda parcial

### Funcionalidad Técnica
- [ ] **Paginación mejorada**
  - Mantener paginación existente (page, size, sort)
  - Ordenamiento por múltiples campos
  - Información de metadatos en la respuesta

- [ ] **Validación de parámetros**
  - Validar rangos de fechas (before debe ser posterior a after)
  - Validar IDs de usuarios y proyectos existentes
  - Mensajes de error claros para parámetros inválidos

- [ ] **Performance**
  - Consultas optimizadas (usar índices apropiados)
  - Máximo 500ms de tiempo de respuesta para 10,000 registros
  - Soporte para hasta 50 resultados por página

## 🔧 Especificación Técnica

### Endpoint Mejorado
```
GET /api/tasks/advanced-search
```

### Parámetros de Consulta
| Parámetro | Tipo | Descripción | Ejemplo |
|-----------|------|-------------|---------|
| `status` | String[] | Estados de tarea (múltiples) | `?status=TODO&status=IN_PROGRESS` |
| `assignedTo` | Long | ID del usuario asignado | `?assignedTo=1` |
| `unassigned` | Boolean | Solo tareas sin asignar | `?unassigned=true` |
| `projectId` | Long | ID del proyecto | `?projectId=2` |
| `createdAfter` | Date | Fecha mínima de creación | `?createdAfter=2024-01-01` |
| `createdBefore` | Date | Fecha máxima de creación | `?createdBefore=2024-12-31` |
| `dueAfter` | Date | Fecha mínima de vencimiento | `?dueAfter=2024-03-01` |
| `dueBefore` | Date | Fecha máxima de vencimiento | `?dueBefore=2024-03-31` |
| `overdue` | Boolean | Solo tareas vencidas | `?overdue=true` |
| `search` | String | Texto a buscar en título/descripción | `?search=homepage` |
| `page` | Integer | Número de página (0-based) | `?page=0` |
| `size` | Integer | Tamaño de página (máx 50) | `?size=20` |
| `sortBy` | String | Campo para ordenar | `?sortBy=createdAt` |
| `sortDir` | String | Dirección (asc/desc) | `?sortDir=desc` |

### Respuesta Esperada
```json
{
  "content": [
    {
      "id": 1,
      "title": "Task Title",
      "description": "Task Description",
      "status": "IN_PROGRESS",
      "createdAt": "2024-03-01T10:00:00Z",
      "dueDate": "2024-03-15T23:59:59Z",
      "assignedTo": {
        "id": 1,
        "name": "John Doe",
        "email": "john@example.com"
      },
      "project": {
        "id": 1,
        "name": "Website Redesign"
      }
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "sorted": true,
      "unsorted": false
    }
  },
  "totalElements": 25,
  "totalPages": 2,
  "last": false,
  "size": 20,
  "number": 0,
  "numberOfElements": 20,
  "first": true,
  "empty": false
}
```

## 🗂️ Archivos a Modificar/Crear

### Nuevos Archivos
- `src/main/java/com/workshop/taskmanager/dto/TaskFilterDto.java`
- `src/test/java/com/workshop/taskmanager/service/TaskFilterServiceTest.java`

### Archivos a Modificar
- `src/main/java/com/workshop/taskmanager/controller/TaskController.java` - Mejorar endpoint advanced-search
- `src/main/java/com/workshop/taskmanager/service/TaskService.java` - Agregar método searchTasksWithFilters()
- `src/main/java/com/workshop/taskmanager/repository/TaskRepository.java` - Agregar queries con Criteria API
- `src/test/java/com/workshop/taskmanager/controller/TaskControllerTest.java` - Tests para nuevos filtros

## 🧪 Casos de Prueba

### Casos de Prueba Funcionales
1. **Filtro por estado múltiple**: Buscar tareas TODO e IN_PROGRESS
2. **Rango de fechas**: Tareas creadas en el último mes
3. **Combinación de filtros**: Tareas asignadas a usuario específico en proyecto específico
4. **Tareas vencidas**: Solo tareas con dueDate anterior a hoy
5. **Búsqueda de texto**: Tareas que contengan "homepage" en título o descripción
6. **Tareas sin asignar**: Filtrar solo tareas donde assignedTo es null

### Casos de Prueba de Performance
1. **Volumen alto**: 10,000 tareas, respuesta < 500ms
2. **Múltiples filtros**: Combinación de 5+ filtros simultáneos
3. **Paginación grande**: Navegar hasta página 100 con 50 elementos por página

### Casos de Prueba de Validación
1. **Fechas inválidas**: createdAfter posterior a createdBefore
2. **IDs inexistentes**: Usuario o proyecto que no existe
3. **Parámetros vacíos**: Manejar parámetros nulos o vacíos
4. **Caracteres especiales**: Búsqueda con caracteres unicode y símbolos

## 🔗 Dependencias

- JPA Criteria API para queries dinámicas
- Spring Data JPA Specification
- Validation API para validación de parámetros

## 🚀 Definición de Terminado (DoD)

- [ ] Código implementado y funcionando según especificación
- [ ] Tests unitarios con cobertura > 90%
- [ ] Tests de integración para todos los endpoints
- [ ] Documentación de API actualizada
- [ ] Performance tests pasando (< 500ms)
- [ ] Code review completado y aprobado
- [ ] Funcionalidad probada en entorno de desarrollo

## 📚 Referencias

- [Spring Data JPA Specifications](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#specifications)
- [JPA Criteria API](https://docs.oracle.com/javaee/7/tutorial/persistence-criteria.htm)
- [API Design Best Practices](https://restfulapi.net/resource-naming/)

---

**Estimación:** 5 Story Points
**Prioridad:** Alta
**Sprint:** Sprint 1
**Asignado a:** Claude Code Workshop Team