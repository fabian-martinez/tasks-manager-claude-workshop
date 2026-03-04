# 🎓 Claude Code Workshop - Guía del Facilitador

> **Guía completa para facilitar el workshop "Claude Code Work Flow" usando la Task Manager API**

## 📋 Resumen del Workshop

### Objetivos de Aprendizaje
Al finalizar este workshop, los participantes podrán:
- ✅ Entender el flujo completo de trabajo con Claude Code
- ✅ Aplicar Claude Code para analizar aplicaciones existentes
- ✅ Usar Claude Code para implementar funcionalidades nuevas siguiendo especificaciones
- ✅ Integrar Claude Code en workflows de desarrollo reales
- ✅ Evaluar el impacto de Claude Code en productividad de desarrollo

### Audiencia Target
- 👩‍💻 **Desarrolladores Senior** - Que quieren acelerar su productividad
- 🏗️ **Arquitectos de Software** - Interesados en herramientas de análisis de código
- 👥 **Líderes Técnicos** - Evaluando herramientas para sus equipos
- 🎓 **Estudiantes Avanzados** - Con conocimiento de Java/Spring Boot

### Duración y Formato
- ⏱️ **Duración total**: 2-3 horas
- 🎯 **Formato**: Demostración interactiva con ejercicios prácticos
- 👥 **Tamaño grupo**: 5-20 participantes
- 💻 **Modalidad**: Presencial o virtual (con screen sharing)

## 🏗️ Preparación Pre-Workshop

### Para el Facilitador

#### 1. Verificación del Entorno
```bash
# Verificar que la aplicación funciona
cd task-manager-api
mvn clean install
mvn spring-boot:run

# Probar endpoints clave
curl http://localhost:8080/actuator/health
curl http://localhost:8080/api/users
curl http://localhost:8080/api/tasks
```

#### 2. Preparar Claude Code
- ✅ Cuenta de Claude Code configurada y funcionando
- ✅ Verificar acceso a la aplicación desde Claude Code
- ✅ Probar algunos comandos básicos para familiarizarse

#### 3. Material de Respaldo
- 📄 Copiar las 5 stories de `/mock-jira-stories/` a un lugar accesible
- 🖼️ Screenshots de antes/después para comparaciones
- 📊 Métricas de performance para mostrar mejoras

### Para los Participantes

#### Prerrequisitos Técnicos
- ☕ **Java 17+** instalado y configurado
- 📦 **Maven 3.6+** en PATH
- 💻 **IDE** (IntelliJ IDEA, VSCode, Eclipse)
- 🌐 **Navegador web** moderno
- 🔧 **Cliente HTTP** (Postman, curl, o similar)

#### Conocimientos Previos
- 🏗️ **Spring Boot** - Nivel intermedio
- 🗄️ **JPA/Hibernate** - Conceptos básicos
- 🌐 **APIs REST** - Diseño y consumo
- 🧪 **Testing** - JUnit básico
- 📦 **Maven** - Comandos básicos

## 📚 Agenda Detallada del Workshop

### 🎬 Fase 1: Introducción y Setup (20 minutos)

#### Presentación Inicial (10 min)
```markdown
## ¿Qué es Claude Code?
- Asistente de desarrollo de software basado en IA
- Capaz de analizar, entender, planificar e implementar código
- Integración con entornos de desarrollo reales

## ¿Por qué este Workshop?
- Demostrar capacidades reales en proyectos complejos
- Mostrar flujo completo: análisis → planificación → implementación
- Evaluuar impacto en productividad de desarrollo
```

#### Demo de la Aplicación Base (10 min)
```bash
# 1. Iniciar la aplicación
mvn spring-boot:run

# 2. Mostrar endpoints funcionando
curl http://localhost:8080/api/users | jq
curl http://localhost:8080/api/tasks | jq

# 3. Acceder a H2 Console
# URL: http://localhost:8080/h2-console
# Mostrar datos de ejemplo

# 4. Explicar arquitectura
tree src/main/java/com/workshop/taskmanager/
```

**Puntos clave a destacar:**
- ✅ Aplicación funcional con datos de prueba
- ✅ Arquitectura típica de aplicaciones empresariales
- ✅ Base de tests existente
- ❌ Funcionalidades importantes pendientes (las 5 stories)

### 🔍 Fase 2: Análisis con Claude Code (25 minutos)

#### Demostración: Análisis de Codebase (15 min)

**Comando inicial:**
```
"Analiza esta aplicación Java Spring Boot y explícame su arquitectura,
funcionalidades implementadas, y áreas donde podríamos agregar mejoras."
```

**Mostrar cómo Claude Code:**
- 🔍 Examina la estructura de directorios
- 📖 Lee y entiende el código existente
- 🏗️ Identifica patrones arquitectónicos
- 📊 Analiza dependencias y configuración
- 🎯 Sugiere áreas de mejora

#### Ejercicio Práctico: Exploración Dirigida (10 min)

**Pedir a Claude Code que analice específicamente:**
```
"Revisa el TaskController y TaskService. ¿Qué funcionalidades de
filtrado ya están implementadas y cuáles podrían mejorarse?"
```

**Objetivo:** Que los participantes vean cómo Claude Code puede entender contexto específico y dar recomendaciones precisas.

### 🎯 Fase 3: Implementación Guiada - Story 1 (45 minutos)

#### Presentar el Caso de Uso (5 min)

```markdown
## Escenario Real
El Product Manager acaba de crear TASK-001 en Jira:
"Implementar Filtros Avanzados para Tareas"

Como desarrollador, necesitamos:
1. Entender los requisitos exactos
2. Planificar la implementación
3. Implementar la funcionalidad
4. Probar que funciona correctamente
```

#### Demostrar Flujo Completo (30 min)

**Paso 1: Análisis de Requisitos (5 min)**
```
"Lee la especificación en src/main/resources/mock-jira-stories/TASK-001-filters.md
y ayúdame a entender exactamente qué necesito implementar."
```

**Paso 2: Planificación (10 min)**
```
"Basándote en el código existente y los requisitos de TASK-001,
propón un plan de implementación detallado incluyendo qué archivos
necesito modificar y qué nuevos archivos crear."
```

**Paso 3: Implementación (10 min)**
```
"Implementa los filtros avanzados según el plan que propusiste.
Comienza creando el TaskFilterDto y luego modifica el TaskService
y TaskController según sea necesario."
```

**Paso 4: Testing (5 min)**
```
"Crea tests unitarios para validar que los nuevos filtros
funcionan correctamente según los criterios de aceptación."
```

#### Validación en Vivo (10 min)

```bash
# Compilar y ejecutar tests
mvn clean compile
mvn test

# Probar los nuevos endpoints
curl "http://localhost:8080/api/tasks/advanced-search?status=TODO&assignedTo=1"
curl "http://localhost:8080/api/tasks/advanced-search?createdAfter=2024-01-01&createdBefore=2024-12-31"
```

### 🚀 Fase 4: Ejercicio Independiente - Story 2 (30 minutos)

#### Setup del Ejercicio (5 min)

```markdown
## Tu turno: TASK-002 - Sistema de Notificaciones

Trabajarás en parejas o individualmente para implementar
el sistema de notificaciones por email usando Claude Code.

### Objetivo
Demostrar que puedes usar Claude Code independientemente
para implementar funcionalidades complejas.

### Recursos
- Story completa en TASK-002-notifications.md
- Código base ya implementado en Fase 3
- Claude Code como tu asistente principal
```

#### Trabajo Independiente (20 min)

**Los participantes deben:**
1. 📖 Leer TASK-002-notifications.md completamente
2. 🤖 Usar Claude Code para planificar la implementación
3. ⚒️ Implementar al menos la funcionalidad básica de envío de emails
4. 🧪 Crear tests básicos para validar funcionamiento
5. 📋 Documentar cualquier decisión o limitación encontrada

**Comandos sugeridos para empezar:**
```
"Analiza TASK-002-notifications.md y propón cómo integrar
un sistema de notificaciones en esta aplicación existente."

"Crea la entidad Notification y NotificationService básicos
para empezar con el sistema de notificaciones."
```

#### Revisión Grupal (5 min)

- 🔄 Cada pareja/participante comparte su enfoque
- ❓ Discusión de diferentes soluciones encontradas
- 🎯 Identificación de patrones comunes en el uso de Claude Code

### 🎯 Fase 5: Demostración Avanzada (15 minutos)

#### Casos de Uso Complejos (10 min)

**Mostrar capacidades avanzadas:**

```
"Refactoriza el TaskService para usar el patrón Repository
con Specifications de Spring Data JPA para hacer las
consultas más flexibles y performantes."
```

```
"Analiza posibles vulnerabilidades de seguridad en esta
aplicación y propón implementación de medidas de protección."
```

```
"Crea un script de migración de datos para convertir esta
aplicación H2 a PostgreSQL en producción."
```

#### Integración con Herramientas (5 min)

- 🔧 **Claude Code + IDE**: Mostrar workflow típico
- 🐛 **Debugging**: Cómo usar Claude Code para encontrar bugs
- 📊 **Code Review**: Usar Claude Code como revisor de código
- 📚 **Documentación**: Generar documentación automática

### 📊 Fase 6: Evaluación y Cierre (15 minutos)

#### Métricas de Impacto (5 min)

**Comparar desarrollo tradicional vs. con Claude Code:**

| Aspecto | Sin Claude Code | Con Claude Code | Mejora |
|---------|----------------|----------------|--------|
| **Análisis inicial** | 30-45 min | 5-10 min | 70% menos tiempo |
| **Planificación** | 45-60 min | 10-15 min | 75% menos tiempo |
| **Implementación** | 3-4 horas | 1-2 horas | 50% menos tiempo |
| **Testing** | 1-2 horas | 30-60 min | 50% menos tiempo |
| **Documentación** | 30-45 min | 5-10 min | 80% menos tiempo |

#### Retroalimentación Grupal (5 min)

**Preguntas clave:**
- ❓ ¿Qué les sorprendió más de las capacidades de Claude Code?
- 💡 ¿En qué aspectos de su trabajo actual podría ser más útil?
- 🚫 ¿Qué limitaciones identificaron?
- 🎯 ¿Cómo cambiaría su flujo de desarrollo actual?

#### Próximos Pasos (5 min)

**Recursos para continuar:**
- 📚 Documentación oficial de Claude Code
- 🎓 Cursos adicionales recomendados
- 👥 Comunidad y soporte
- 🔄 Implementación en proyectos reales

## 🎯 Objetivos de Aprendizaje por Fase

### ✅ Después de Fase 1-2: Análisis
Los participantes entienden:
- Cómo Claude Code puede analizar codebases complejos
- El valor de tener contexto completo antes de implementar
- Diferencias entre análisis manual vs. asistido por IA

### ✅ Después de Fase 3: Implementación Guiada
Los participantes pueden:
- Usar Claude Code para planificar implementaciones
- Seguir el flujo: requisitos → plan → código → tests
- Validar que la implementación cumple especificaciones

### ✅ Después de Fase 4: Ejercicio Independiente
Los participantes demuestran:
- Autonomía usando Claude Code
- Capacidad de trabajar con especificaciones complejas
- Adaptación del flujo aprendido a nuevos problemas

### ✅ Después de Fase 5-6: Evaluación
Los participantes pueden:
- Evaluar el impacto real en productividad
- Identificar casos de uso óptimos para Claude Code
- Planificar integración en sus proyectos actuales

## 🛠️ Herramientas y Recursos

### Durante el Workshop

#### Para Demostración
- 💻 **Proyector/Screen sharing** - Para mostrar código en vivo
- 🔄 **Git** - Para mostrar diffs de cambios
- 📊 **Postman/curl** - Para probar APIs
- 🖥️ **Terminal** - Para comandos Maven y ejecución

#### Para Participantes
- 📝 **Notebook/laptop** - Para seguir ejercicios
- 📚 **Acceso a stories** - Impresas o digitales
- ⏱️ **Timer** - Para gestión de tiempo en ejercicios
- 💬 **Chat grupal** - Para compartir resultados

### Post-Workshop

#### Recursos de Seguimiento
- 📧 **Email con links** - Documentación y recursos
- 💾 **Código completo** - Repositorio con implementaciones finales
- 📊 **Métricas** - Datos de productividad y comparaciones
- 📅 **Calendario** - Sesiones de seguimiento opcionales

## 📈 Métricas de Éxito del Workshop

### Indicadores Durante el Workshop
- 👥 **Participación activa** - % de participantes que completan ejercicios
- ⏱️ **Tiempo de implementación** - Comparación vs. estimaciones tradicionales
- ✅ **Calidad de output** - Código generado cumple criterios de aceptación
- 🤝 **Colaboración** - Interacciones productivas entre participantes

### Evaluación Post-Workshop (1 semana después)
- 📊 **NPS Score** - Satisfacción general con el workshop
- 💼 **Adopción** - % que empezó a usar Claude Code en proyectos reales
- 🎯 **Casos de uso** - Tipos de tareas donde aplicaron aprendizajes
- 📈 **Productividad percibida** - Mejora reportada en velocidad de desarrollo

### Objetivos Cuantitativos
- **Satisfacción**: NPS > 8.0
- **Adopción**: >60% usa Claude Code en siguiente proyecto
- **Tiempo de desarrollo**: 40-60% reducción en tareas similares
- **Calidad de código**: Mantener/mejorar estándares actuales

## 🚨 Contingencias y Troubleshooting

### Problemas Técnicos Comunes

#### Aplicación no inicia
```bash
# Verificar Java version
java -version  # Debe ser 17+

# Limpiar y reinstalar
mvn clean install -U

# Verificar puerto 8080 libre
lsof -ti:8080  # Si hay proceso, matar con kill -9 PID
```

#### Claude Code no responde apropiadamente
- 🔄 **Reiniciar sesión** de Claude Code
- 📝 **Reformular comandos** con más contexto específico
- 🔍 **Verificar que Claude tiene acceso** a todos los archivos del proyecto

#### Participantes con problemas de setup
- 👥 **Emparejamiento** con participantes que funcionan
- 💻 **Compartir pantalla** para debug colectivo
- 🎯 **Enfoque en conceptos** si persisten problemas técnicos

### Adaptaciones por Audiencia

#### Para Desarrolladores Junior
- ➕ **Más tiempo en explicación** de conceptos Spring Boot
- 📚 **Recursos adicionales** para entender el código base
- 👥 **Más trabajo en parejas** con developers senior

#### Para Arquitectos/Líderes Técnicos
- 🏗️ **Enfoque en decisiones arquitectónicas** tomadas por Claude Code
- 📊 **Métricas de impacto** en equipos y proyectos
- 🎯 **Casos de uso estratégicos** para adopción organizacional

#### Para Equipos Remotos
- 🎥 **Grabación de sesiones** para revisión posterior
- 💬 **Breakout rooms** para ejercicios independientes
- 📱 **Herramientas colaborativas** (Miro, Figma) para mapeo visual

---

## 🎉 ¡Éxito del Workshop!

Este workshop está diseñado para ser **práctico, interactivo, y directamente aplicable** al trabajo diario de desarrollo de software. Al final, los participantes no solo habrán visto una demostración, sino que habrán **experimentado personalmente** el poder de Claude Code en escenarios reales.

**¡Que comience la transformación del desarrollo de software!** 🚀