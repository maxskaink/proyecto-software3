# Sistema de Evaluación de Resultados de Aprendizaje (SERA)

Sistema integral para la gestión académica de competencias, resultados de aprendizaje y evaluación mediante rúbricas en programas universitarios.

## 👥 Equipo de Desarrollo
- **Miguel Angel Calambas Vivas**
- **Esteban Santiago Escadon Causaya** 
- **Julian David Meneses**
- **Jesus David Iles Pena**

---

## 🏗️ Arquitectura del Sistema

### Descripción General
SERA implementa una **arquitectura de microservicios** con las siguientes características:

- **Frontend**: Angular 19 con interfaz moderna y responsiva
- **Backend**: Microservicios en Spring Boot
- **Base de Datos**: PostgreSQL compartida
- **Contenerización**: Docker y Docker Compose
- **Autenticación**: Firebase Authentication

### Tecnologías Utilizadas

| Componente | Tecnología | Versión |
|------------|------------|---------|
| Frontend | Angular | 19.2.12 |
| Backend Core | Spring Boot | 3.4.4 |
| Backend Micro | Spring Boot | 3.5.0 |
| Base de Datos | PostgreSQL | 15 |
| Runtime | Java | 21/23 |
| Contenedores | Docker & Docker Compose | Latest |
| Autenticación | Firebase | 19.2.0 |

---

## 🎨 Patrones de Diseño Implementados

### Backend (Spring Boot Microservicios)

#### 1. **Repository Pattern**
Separación clara entre la lógica de negocio y el acceso a datos mediante interfaces de repositorio.

**Implementación:**
- `ProgramCompetencyRepository`, `SubjectRepository`, `RubricRepository`
- Uso de Spring Data JPA para abstracción de persistencia
- Operaciones CRUD estandarizadas y consultas personalizadas

```java
@Repository
public interface ProgramCompetencyRepository extends JpaRepository<ProgramCompetency, Long> {
    List<ProgramCompetency> findByTermId(Long termId);
}
```

#### 2. **Service Layer Pattern**
Encapsulación de la lógica de negocio en servicios dedicados con responsabilidades específicas.

**Implementación:**
- `ProgramCompetencyService`, `SubjectService`, `RubricService`
- Transacciones manejadas a nivel de servicio
- Validaciones de negocio centralizadas

#### 3. **Dependency Injection**
Gestión automática de dependencias mediante el contenedor IoC de Spring.

**Implementación:**
- Inyección de repositorios en servicios mediante `@Autowired`
- Configuración declarativa de beans
- Desacoplamiento entre componentes

#### 4. **Mapper Pattern**
Transformación entre entidades de base de datos y objetos de dominio/DTOs.

**Implementación:**
- `ProgramCompetencyMapper`, `SubjectMapper`, `RubricMapper`
- Separación entre modelos de datos y modelos de negocio
- Conversión bidireccional de objetos

```java
@Component
public class ProgramCompetencyMapper {
    public ProgramCompetencyDomain toDomain(ProgramCompetency entity) {
        // Transformación entity -> domain
    }
}
```

#### 5. **MVC Pattern (Model-View-Controller)**
Arquitectura en capas con separación clara de responsabilidades.

**Implementación:**
- **Controllers**: `ProgramCompetencyController`, `SubjectController`
- **Models**: Entidades JPA y objetos de dominio
- **Services**: Lógica de negocio intermedia

#### 6. **Factory Pattern**
Creación de objetos mediante configuración de Spring Boot.

**Implementación:**
- Configuración de beans mediante `@Configuration`
- Factory methods para componentes específicos
- Gestión del ciclo de vida de objetos

#### 7. **Exception Handling Pattern**
Manejo centralizado de excepciones con respuestas HTTP estandarizadas.

**Implementación:**
- `@ControllerAdvice` para manejo global de excepciones
- Excepciones personalizadas por dominio
- Respuestas de error estructuradas

### Frontend (Angular)

#### 1. **Component-Service Architecture**
Separación entre componentes de presentación y servicios de datos.

**Implementación:**
- Componentes focalizados en UI: `ProgramCompetencyComponent`, `SubjectListComponent`
- Servicios para lógica de negocio: `ProgramCompetencyService`, `SubjectService`
- Comunicación unidireccional de datos

#### 2. **Dependency Injection**
Sistema de inyección de dependencias nativo de Angular.

**Implementación:**
- Servicios inyectados en constructores de componentes
- Providers configurados a nivel de módulo
- Singleton pattern para servicios

```typescript
constructor(
  private programCompetencyService: ProgramCompetencyService,
  private router: Router
) {}
```

#### 3. **Observer Pattern (RxJS)**
Programación reactiva para manejo de eventos asíncronos.

**Implementación:**
- Observables para llamadas HTTP
- Subscription management en componentes
- Operadores RxJS para transformación de datos

```typescript
this.programCompetencyService.getCompetencies()
  .subscribe(competencies => {
    this.competencies = competencies;
  });
```

#### 4. **Module Pattern**
Organización modular de funcionalidades relacionadas.

**Implementación:**
- `AppModule` como módulo raíz
- Feature modules para agrupación lógica
- Lazy loading de módulos

#### 5. **Template Pattern**
Plantillas HTML reutilizables con interpolación de datos.

**Implementación:**
- Templates separados en archivos `.html`
- Data binding bidireccional
- Directivas estructurales (`*ngFor`, `*ngIf`)

#### 6. **Guard Pattern**
Protección de rutas mediante guards de navegación.

**Implementación:**
- `CanActivate` guards para protección de rutas
- Verificación de autenticación
- Redirección condicional

### Patrones Arquitecturales Generales

#### 1. **Microservices Pattern**
División del sistema en servicios independientes y especializados.

**Implementación:**
- `CoreService`: Gestión de competencias y rúbricas
- `microSubject`: Gestión específica de asignaturas
- Comunicación mediante APIs REST

#### 2. **API Gateway Pattern** (Implícito)
Punto único de entrada para el frontend hacia los microservicios.

**Implementación:**
- Angular como gateway implícito
- Routing interno hacia diferentes servicios
- Manejo centralizado de autenticación

#### 3. **Database per Service**
Base de datos compartida con acceso controlado por servicio.

**Implementación:**
- PostgreSQL compartida entre microservicios
- Responsabilidades de datos divididas por dominio
- Esquemas separados lógicamente

### Beneficios de los Patrones Implementados

- **Mantenibilidad**: Código organizado y fácil de modificar
- **Escalabilidad**: Servicios independientes y componentes modulares
- **Testabilidad**: Separación de responsabilidades facilita pruebas
- **Reutilización**: Componentes y servicios reutilizables
- **Desacoplamiento**: Baja dependencia entre módulos
- **Flexibilidad**: Facilidad para agregar nuevas funcionalidades

---

## 🚀 Cómo Ejecutar el Proyecto

### Prerrequisitos
- Docker y Docker Compose instalados
- Puertos 4200, 5432, 8080, 8081 disponibles

### Pasos de Ejecución

1. **Clonar el repositorio**
   ```bash
   git clone <repository-url>
   cd proyecto-final
   ```

2. **Configurar credenciales de Firebase**
   - Crear archivo de credenciales Firebase en `CoreService/src/main/resources/firebase/`
   - Asegurar que el archivo está en `.gitignore`

3. **Construir y ejecutar con Docker Compose**
   ```bash
   # Construir e iniciar todos los servicios
   docker-compose up --build

   # Ejecutar en segundo plano
   docker-compose up -d --build

   # Ver logs
   docker-compose logs -f
   ```

4. **Acceder a la aplicación**
   - **Frontend**: http://localhost:4200
   - **Core Service API**: http://localhost:8080
   - **Micro Subject API**: http://localhost:8081
   - **Base de Datos**: localhost:5432

### Desarrollo Local

Para desarrollo, puedes ejecutar componentes individuales:

```bash
# Frontend Angular
cd FrontendAngular
npm install
npm start

# Core Service
cd CoreService
./mvnw spring-boot:run

# Micro Subject
cd microSubject
./mvnw spring-boot:run
```

### Parar la Aplicación
```bash
docker-compose down

# Limpiar volúmenes (eliminará datos)
docker-compose down -v
```

---

## 🎯 Funcionalidades Principales

### 1. Gestión de Competencias de Programa
- ✅ Crear, editar y eliminar competencias de programa
- ✅ Definir niveles: Básico, Intermedio, Avanzado
- ✅ Asociar resultados de aprendizaje de programa
- ✅ Gestión por períodos académicos

### 2. Gestión de Asignaturas
- ✅ Administración de asignaturas del programa
- ✅ Asignación de competencias de asignatura (máximo 3 por asignatura)
- ✅ Creación de resultados de aprendizaje específicos
- ✅ Gestión de períodos académicos (2024-1, 2024-2, 2025-1)

### 3. Sistema de Rúbricas de Evaluación
- ✅ Creación de rúbricas personalizadas por resultado de aprendizaje
- ✅ Definición de criterios con pesos específicos (suma 100%)
- ✅ Niveles de evaluación: Excelente, Competente, En Desarrollo, Insuficiente
- ✅ Criterios predefinidos por área:
  - **Programación**: Corrección funcional, legibilidad, eficiencia
  - **Estructuras de Datos**: Implementación, manejo de casos borde, operaciones básicas
  - **Algoritmos**: Análisis matemático, identificación de cuellos de botella, comparación de complejidades

### 4. Gestión de Docentes
- ✅ Registro y edición de información docente
- ✅ Asignación de docentes a asignaturas por período
- ✅ Gestión de roles y tipos de docente
- ✅ Validación de datos académicos

### 5. Sistema de Evaluación
- ✅ Evaluación basada en rúbricas
- ✅ Seguimiento por competencias y resultados de aprendizaje
- ✅ Reportes de progreso académico
- ✅ Análisis de cumplimiento de competencias

### 6. Interfaz de Usuario
- ✅ Diseño responsive y moderno
- ✅ Navegación intuitiva
- ✅ Formularios validados
- ✅ Componentes reutilizables
- ✅ Alertas y mensajes informativos

---

## 📊 Estructura de la Base de Datos

### Entidades Principales
- **Período**: Gestión de períodos académicos
- **Asignatura**: Materias del programa
- **Competencia Programa**: Competencias generales del programa
- **Competencia Asignatura**: Competencias específicas por materia
- **Resultado de Aprendizaje**: Objetivos específicos de aprendizaje
- **Rúbrica**: Instrumentos de evaluación
- **Criterio**: Aspectos específicos a evaluar
- **Nivel**: Escalas de calificación por criterio

### Datos Precargados
El sistema incluye datos de ejemplo para:
- 3 períodos académicos (2024-1, 2024-2, 2025-1)
- 3 asignaturas: Programación I, Estructuras de Datos, Algoritmos
- 2 competencias de programa
- 6 competencias de asignatura (2 por asignatura)
- 12 resultados de aprendizaje
- 12 rúbricas con criterios específicos

---

## 🔧 Configuración Adicional

### Variables de Entorno
```yaml
# Base de datos
POSTGRES_DB=mi_basedatos
POSTGRES_USER=admin
POSTGRES_PASSWORD=admin123

# Spring Boot
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/mi_basedatos
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

### Volúmenes Docker
- `pgdata`: Persistencia de datos PostgreSQL
- `maven-repo`: Cache de dependencias Maven (Core Service)
- `maven-repo-micro`: Cache de dependencias Maven (Micro Service)

### Puertos Utilizados
- **4200**: Frontend Angular
- **5432**: PostgreSQL
- **8080**: Core Service
- **8081**: Micro Subject Service

---

## 📖 Documentación API

### Core Service Endpoints
- `/program/competency` - Gestión de competencias de programa
- `/program/outcome` - Resultados de aprendizaje de programa  
- `/subject/competency` - Competencias de asignatura
- `/subject/outcome` - Resultados de aprendizaje de asignatura
- `/rubric` - Gestión de rúbricas
- `/criterion` - Criterios de evaluación
- `/term` - Períodos académicos

### Documentación Completa
Ver archivos `ENDPOINTS.md` en las carpetas del proyecto para documentación detallada de la API REST.

---

## 🛠️ Desarrollo y Mantenimiento

### Estructura del Proyecto
```
proyecto-final/
├── docker-compose.yml          # Configuración de servicios
├── CoreService/               # Servicio principal Spring Boot
├── microSubject/             # Microservicio de asignaturas
├── FrontendAngular/          # Aplicación Angular
└── README.md                 # Este archivo
```

### Comandos Útiles Docker
```bash
# Ver logs de un servicio específico
docker-compose logs -f core-service

# Reiniciar un servicio
docker-compose restart core-service

# Entrar al contenedor de base de datos
docker exec -it db_postgres psql -U admin -d mi_basedatos

# Limpiar cache de Maven
docker-compose down
docker volume rm proyecto-final_maven-repo
```

### Consideraciones de Seguridad
- Las credenciales de Firebase NO deben subirse al repositorio
- Usar variables de entorno para datos sensibles
- El archivo `.gitignore` excluye archivos de credenciales

---

## 🔄 Actualizaciones y Mejoras Futuras

### Funcionalidades Planeadas
- Sistema de reportes avanzado
- Exportación de resultados en PDF/Excel
- Dashboard analítico para coordinadores
- Integración con sistemas institucionales
- Módulo de evaluación en línea para estudiantes

### Optimizaciones Técnicas
- Implementación de cache Redis
- Mejoras en la seguridad JWT
- Optimización de consultas a base de datos
- Implementación de tests automatizados

---

## 📞 Soporte y Contacto

Para problemas técnicos o consultas sobre el sistema, contactar al equipo de desarrollo a través de los canales institucionales.

**Versión**: 1.0.0  
**Última actualización**: Junio 2025
