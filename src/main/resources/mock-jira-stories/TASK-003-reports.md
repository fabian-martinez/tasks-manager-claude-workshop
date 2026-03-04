# TASK-003: Sistema de Reportes y Estadísticas

## 📋 User Story

**Como** gerente de proyecto y líder de equipo
**Quiero** generar reportes y visualizar estadísticas sobre el progreso de tareas y rendimiento del equipo
**Para** tomar decisiones informadas y monitorear la productividad del proyecto

## 🎯 Objetivos de Negocio

- Proporcionar visibilidad sobre el progreso de proyectos y equipos
- Identificar cuellos de botella y áreas de mejora
- Facilitar reporting ejecutivo y seguimiento de KPIs
- Mejorar la planificación y asignación de recursos
- Establecer métricas de productividad y calidad

## 📝 Descripción Detallada

El sistema actual no proporciona capacidades de análisis o reporting. Los stakeholders necesitan insights sobre el rendimiento del equipo, progreso de proyectos, y métricas de productividad para tomar decisiones estratégicas y operacionales.

## ✅ Criterios de Aceptación

### Dashboard de Métricas Generales
- [ ] **Resumen de tareas por estado**
  - Contador total de tareas por estado (TODO, IN_PROGRESS, DONE)
  - Porcentaje de completitud por proyecto
  - Tendencia de creación/completitud de tareas (últimos 30 días)

- [ ] **Métricas de tiempo**
  - Tiempo promedio de completitud de tareas
  - Tareas vencidas y próximas a vencer
  - Distribución de tareas por fecha de vencimiento

- [ ] **Métricas de equipo**
  - Carga de trabajo por usuario (tareas asignadas)
  - Productividad por usuario (tareas completadas/período)
  - Colaboración (comentarios por usuario)

### Reportes por Proyecto
- [ ] **Estado del proyecto**
  - Progreso general del proyecto (% completado)
  - Timeline del proyecto con hitos importantes
  - Riesgo de retraso basado en velocidad actual

- [ ] **Distribución de tareas**
  - Tareas por usuario dentro del proyecto
  - Tipos de tareas más comunes
  - Complejidad promedio (basada en tiempo de completitud)

### Reportes por Usuario
- [ ] **Performance individual**
  - Tareas completadas por período (día/semana/mes)
  - Tiempo promedio de completitud por usuario
  - Tareas creadas vs. tareas completadas

- [ ] **Carga de trabajo**
  - Tareas actualmente asignadas
  - Historial de asignaciones
  - Distribución de tiempo por proyecto

### Exportación de Datos
- [ ] **Formatos de exportación**
  - PDF para reportes ejecutivos
  - Excel/CSV para análisis detallado
  - JSON para integración con otras herramientas

- [ ] **Programación de reportes**
  - Generación automática diaria/semanal/mensual
  - Envío por email a stakeholders
  - Almacenamiento histórico de reportes

## 🔧 Especificación Técnica

### Nuevos Endpoints

#### Dashboard General
```
GET /api/reports/dashboard
GET /api/reports/dashboard/summary
```

#### Reportes por Proyecto
```
GET /api/reports/project/{projectId}/summary
GET /api/reports/project/{projectId}/timeline
GET /api/reports/project/{projectId}/team-performance
```

#### Reportes por Usuario
```
GET /api/reports/user/{userId}/performance
GET /api/reports/user/{userId}/workload
GET /api/reports/team/productivity
```

#### Exportación
```
GET /api/reports/export/project/{projectId}?format=pdf
GET /api/reports/export/user/{userId}?format=excel
POST /api/reports/schedule
```

### DTOs de Respuesta

#### Dashboard Summary
```java
public class DashboardSummaryDto {
    private TaskStatusSummary taskSummary;
    private List<ProjectProgress> projectsProgress;
    private TeamProductivity teamMetrics;
    private List<UpcomingDeadline> upcomingDeadlines;
    private TrendData last30Days;
}

public class TaskStatusSummary {
    private long totalTasks;
    private long todoTasks;
    private long inProgressTasks;
    private long completedTasks;
    private double completionRate;
}

public class ProjectProgress {
    private Long projectId;
    private String projectName;
    private long totalTasks;
    private long completedTasks;
    private double progressPercentage;
    private LocalDate estimatedCompletion;
    private String riskLevel; // "LOW", "MEDIUM", "HIGH"
}
```

#### User Performance
```java
public class UserPerformanceDto {
    private Long userId;
    private String userName;
    private long tasksCompleted;
    private long tasksAssigned;
    private double averageCompletionTime; // in hours
    private double productivityScore;
    private List<TimeSeriesData> weeklyProductivity;
}
```

### Métricas Calculadas

#### Productividad del Usuario
```sql
-- Tareas completadas en los últimos 30 días
SELECT u.id, u.name, COUNT(t.id) as completed_tasks
FROM users u
LEFT JOIN tasks t ON u.id = t.assigned_to_id
WHERE t.status = 'DONE'
  AND t.completed_at >= DATEADD('DAY', -30, CURRENT_TIMESTAMP)
GROUP BY u.id, u.name;

-- Tiempo promedio de completitud
SELECT AVG(DATEDIFF('HOUR', t.created_at, t.completed_at)) as avg_completion_hours
FROM tasks t
WHERE t.status = 'DONE'
  AND t.assigned_to_id = ?;
```

#### Progreso del Proyecto
```sql
-- Porcentaje de completitud por proyecto
SELECT p.id, p.name,
       COUNT(t.id) as total_tasks,
       COUNT(CASE WHEN t.status = 'DONE' THEN 1 END) as completed_tasks,
       (COUNT(CASE WHEN t.status = 'DONE' THEN 1 END) * 100.0 / COUNT(t.id)) as completion_percentage
FROM projects p
LEFT JOIN tasks t ON p.id = t.project_id
GROUP BY p.id, p.name;
```

## 🗂️ Archivos a Crear

### Servicios
- `src/main/java/com/workshop/taskmanager/service/ReportService.java`
- `src/main/java/com/workshop/taskmanager/service/DashboardService.java`
- `src/main/java/com/workshop/taskmanager/service/ExportService.java`
- `src/main/java/com/workshop/taskmanager/service/MetricsCalculationService.java`

### DTOs
- `src/main/java/com/workshop/taskmanager/dto/DashboardSummaryDto.java`
- `src/main/java/com/workshop/taskmanager/dto/ProjectProgressDto.java`
- `src/main/java/com/workshop/taskmanager/dto/UserPerformanceDto.java`
- `src/main/java/com/workshop/taskmanager/dto/TeamProductivityDto.java`
- `src/main/java/com/workshop/taskmanager/dto/TimeSeriesDataDto.java`

### Controladores
- `src/main/java/com/workshop/taskmanager/controller/ReportController.java`
- `src/main/java/com/workshop/taskmanager/controller/DashboardController.java`

### Repositorios Extendidos
- `src/main/java/com/workshop/taskmanager/repository/ReportRepository.java`

### Utilidades de Exportación
- `src/main/java/com/workshop/taskmanager/util/PdfExportUtil.java`
- `src/main/java/com/workshop/taskmanager/util/ExcelExportUtil.java`

### Templates para Reportes
- `src/main/resources/report-templates/project-summary.html`
- `src/main/resources/report-templates/user-performance.html`
- `src/main/resources/report-templates/dashboard.html`

### Tests
- `src/test/java/com/workshop/taskmanager/service/ReportServiceTest.java`
- `src/test/java/com/workshop/taskmanager/service/DashboardServiceTest.java`
- `src/test/java/com/workshop/taskmanager/controller/ReportControllerTest.java`

## 🧪 Casos de Prueba

### Funcionalidad de Reportes
1. **Dashboard general**: Métricas correctas para datos de prueba
2. **Progreso de proyecto**: Cálculo correcto de porcentajes de completitud
3. **Performance de usuario**: Métricas precisas de productividad individual
4. **Exportación PDF**: Generación correcta de reporte en PDF
5. **Exportación Excel**: Datos exportados correctamente en formato Excel

### Casos de Datos Límite
1. **Proyecto sin tareas**: Mostrar 0% de progreso apropiadamente
2. **Usuario sin tareas**: Mostrar métricas vacías sin errores
3. **Datos históricos**: Reportes con fechas muy antiguas
4. **Volumen alto**: Performance con 10,000+ tareas

### Performance
1. **Dashboard**: Carga en < 2 segundos con 1000 tareas
2. **Exportación**: Generar PDF de 100 páginas en < 30 segundos
3. **Consultas complejas**: Métricas calculadas en < 5 segundos

## 📊 Ejemplos de Respuesta

### Dashboard Summary
```json
{
  "taskSummary": {
    "totalTasks": 150,
    "todoTasks": 45,
    "inProgressTasks": 35,
    "completedTasks": 70,
    "completionRate": 46.67
  },
  "projectsProgress": [
    {
      "projectId": 1,
      "projectName": "Website Redesign",
      "totalTasks": 25,
      "completedTasks": 15,
      "progressPercentage": 60.0,
      "estimatedCompletion": "2024-04-15",
      "riskLevel": "LOW"
    }
  ],
  "teamMetrics": {
    "totalMembers": 5,
    "averageProductivity": 8.5,
    "topPerformer": {
      "userId": 2,
      "name": "Alice Johnson",
      "tasksCompleted": 25
    }
  },
  "upcomingDeadlines": [
    {
      "taskId": 10,
      "title": "Review Homepage Design",
      "dueDate": "2024-03-10",
      "assignedTo": "Bob Smith",
      "daysRemaining": 3
    }
  ]
}
```

## 🔗 Dependencias

### Maven Dependencies
```xml
<!-- Para exportación PDF -->
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itext7-core</artifactId>
    <version>7.2.5</version>
</dependency>

<!-- Para exportación Excel -->
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>5.2.4</version>
</dependency>

<!-- Para templates HTML a PDF -->
<dependency>
    <groupId>org.xhtmlrenderer</groupId>
    <artifactId>flying-saucer-pdf</artifactId>
    <version>9.1.22</version>
</dependency>
```

## ⚙️ Configuración

### application.yaml
```yaml
app:
  reports:
    enabled: true
    cache-duration: 300 # 5 minutes
    export:
      temp-directory: "/tmp/reports"
      max-file-size: "10MB"
    pdf:
      font-family: "Arial, sans-serif"
      page-size: "A4"
    excel:
      max-rows: 100000
    scheduling:
      enabled: true
      cron-expression: "0 0 8 * * MON" # Every Monday at 8 AM
```

## 🚀 Definición de Terminado (DoD)

- [ ] Todos los endpoints de reportes implementados y funcionando
- [ ] Dashboard con métricas en tiempo real
- [ ] Exportación PDF y Excel funcionando correctamente
- [ ] Cálculos de métricas precisos y validados
- [ ] Performance optimizado para conjuntos de datos grandes
- [ ] Tests unitarios e integración > 90% cobertura
- [ ] Documentación de API completa
- [ ] Templates de reportes responsive y profesionales
- [ ] Manejo de errores robusto
- [ ] Configuración flexible para diferentes entornos

## 📈 Métricas de Éxito

- Tiempo de carga del dashboard < 2 segundos
- Exportación de reportes < 30 segundos para datasets grandes
- Precisión de métricas verificada con datos de prueba
- Satisfacción del usuario con insights proporcionados

---

**Estimación:** 13 Story Points
**Prioridad:** Media
**Sprint:** Sprint 3
**Dependencias:** TASK-001 (datos para reportes), TASK-002 (reportes por email)
**Asignado a:** Claude Code Workshop Team