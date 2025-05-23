# CoreService REST API - Documentación para Angular

Este documento describe los endpoints REST del CoreService, la estructura de los datos JSON que retornan y las interfaces TypeScript recomendadas para consumirlos desde un servicio Angular.

---

## Índice
- [Program Competency](#program-competency)
- [Program Outcome](#program-outcome)
- [Subject Competency](#subject-competency)
- [Subject Outcome](#subject-outcome)
- [Rubric](#rubric)
- [Criterion](#criterion)
- [Level](#level)
- [Term](#term)
- [Manejo de Errores](#manejo-de-errores)
- [Interfaces TypeScript](#interfaces-typescript)

---

## Program Competency

**Base URL:** `/program/competency`

- `POST /program/competency`  → Crea una competencia de programa. **Body:** `ProgramCompetency`  
- `GET /program/competency`  → Lista todas las competencias de programa.  
- `GET /program/competency/{id}`  → Obtiene una competencia de programa por ID.  
- `PUT /program/competency/{id}`  → Actualiza una competencia de programa. **Body:** `ProgramCompetency`  
- `DELETE /program/competency/{id}`  → Elimina una competencia de programa.  

**Ejemplo de respuesta:**
```json
{
  "id": 1,
  "description": "Descripción de la competencia de programa",
  "level": "Nivel de la competencia",
  "programOutcome": {
    "id": 10,
    "description": "Descripción del resultado de aprendizaje del programa"
  },
  "subjectCompetency": [
    {
      "id": 100,
      "description": "Descripción de la competencia de asignatura",
      "level": "Nivel",
      "programCompetencyId": 1
    }
  ]
}
```

---

## Program Outcome

**Base URL:** `/program/outcome`

- `GET /program/outcome`  → Lista todos los resultados de programa.  
- `GET /program/outcome/{id}`  → Obtiene un resultado de programa por ID.  
- `PUT /program/outcome/{id}`  → Actualiza un resultado de programa. **Body:** `ProgramOutcome`  

**Ejemplo de respuesta:**
```json
{
  "id": 10,
  "description": "Descripción del resultado de aprendizaje del programa"
}
```

---

## Subject Competency

**Base URL:** `/subject`

- `POST /subject/{subjectId}/competency`  → Asigna una competencia a una asignatura. **Body:** `InitialSubjectCompetencyDTO`  
- `GET /subject/{subjectId}/competency`  → Lista competencias de una asignatura.  
- `GET /subject/competency/{id}`  → Obtiene una competencia de asignatura por ID.  
- `PUT /subject/competency/{id}`  → Actualiza una competencia de asignatura. **Body:** `SubjectCompetency`  
- `DELETE /subject/competency/{id}`  → Elimina una competencia de asignatura.  

**Ejemplo de respuesta:**
```json
{
  "id": 100,
  "description": "Descripción de la competencia de asignatura",
  "level": "Nivel de la competencia",
  "programCompetencyId": 1
}
```

---

## Subject Outcome

**Base URL:** `/subject`

- `POST /subject/{subjectId}/competency/{competencyId}/outcome`  → Crea un resultado de aprendizaje para una competencia de asignatura. **Body:** `SubjectOutcome`  
- `GET /subject/{subjectId}/outcome?activeTerm=true|false`  → Lista resultados de aprendizaje de una asignatura.  
- `GET /subject/competency/{competencyId}/outcome`  → Lista resultados de aprendizaje por competencia.  
- `GET /subject/outcome/{id}`  → Obtiene un resultado de aprendizaje por ID.  
- `PUT /subject/outcome/{id}`  → Actualiza un resultado de aprendizaje. **Body:** `SubjectOutcome`  
- `DELETE /subject/outcome/{id}`  → Elimina un resultado de aprendizaje.  

**Ejemplo de respuesta:**
```json
{
  "id": 200,
  "description": "Descripción del resultado de aprendizaje de la asignatura",
  "rubric": {
    "id": 300,
    "description": "Descripción de la rúbrica",
    "subjectOutcome": null,
    "criteria": []
  }
}
```

---

## Rubric

**Base URL:** `/rubric`

- `GET /rubric/{rubricId}`  → Obtiene una rúbrica por ID.  
- `GET /rubric/outcome/{outcomeId}`  → Obtiene la rúbrica asociada a un resultado de aprendizaje.  
- `GET /rubric/subject/{subjectId}`  → Lista rúbricas de una asignatura.  
- `POST /rubric/outcome/{outcomeId}`  → Crea una rúbrica para un resultado de aprendizaje. **Body:** `Rubric`  
- `PUT /rubric/{rubricId}`  → Actualiza una rúbrica. **Body:** `Rubric`  

**Ejemplo de respuesta:**
```json
{
  "id": 300,
  "description": "Descripción de la rúbrica",
  "subjectOutcome": {
    "id": 200,
    "description": "Descripción del resultado de aprendizaje de la asignatura",
    "rubric": null
  },
  "criteria": [
    {
      "id": 400,
      "weight": 0.3,
      "name": "Nombre del criterio",
      "rubric": null,
      "levels": [
        {
          "id": 500,
          "category": "Excelente",
          "description": "Descripción del nivel excelente",
          "percentage": 100,
          "criterion": null
        }
      ]
    }
  ]
}
```

---

## Criterion

**Base URL:** `/criterion`

- `GET /criterion/{criterionId}`  → Obtiene un criterio por ID.  
- `GET /criterion/rubric/{rubricId}`  → Lista criterios de una rúbrica.  
- `POST /criterion/rubric/{rubricId}`  → Crea un criterio para una rúbrica. **Body:** `Criterion`  
- `PUT /criterion/{criterionId}`  → Actualiza un criterio. **Body:** `Criterion`  
- `DELETE /criterion/{criterionId}`  → Elimina un criterio.  

**Ejemplo de respuesta:**
```json
{
  "id": 400,
  "weight": 0.3,
  "name": "Nombre del criterio",
  "rubric": {
    "id": 300,
    "description": "Descripción de la rúbrica",
    "subjectOutcome": null,
    "criteria": []
  },
  "levels": [
    {
      "id": 500,
      "category": "Excelente",
      "description": "Descripción del nivel excelente",
      "percentage": 100,
      "criterion": null
    }
  ]
}
```

---

## Level

**Modelo:**
```json
{
  "id": 500,
  "category": "Excelente",
  "description": "Descripción del nivel excelente",
  "percentage": 100,
  "criterion": null
}
```

---

## Term

**Base URL:** `/term`

- `POST /term`  → Crea un periodo académico. **Body:** `Term`  
- `GET /term`  → Lista todos los periodos académicos.  
- `GET /term/active`  → Obtiene el periodo académico activo.  
- `PUT /term/{termId}/activate`  → Activa un periodo académico.  

**Ejemplo de respuesta:**
```json
{
  "id": 1,
  "description": "2025-1"
}
```

---

## Manejo de Errores

Las respuestas de error siguen el formato:
```json
{
  "error": "Descripción general del error",
  "message": "Detalle específico del error"
}
```

---

## Interfaces TypeScript

A continuación se muestran las interfaces recomendadas para consumir los endpoints desde Angular:

```ts
export interface ProgramOutcome {
  id: number;
  description: string;
}

export interface SubjectCompetency {
  id: number;
  description: string;
  level: string;
  programCompetencyId: number;
}

export interface ProgramCompetency {
  id: number;
  description: string;
  level: string;
  programOutcome: ProgramOutcome;
  subjectCompetency: SubjectCompetency[];
}

export interface SubjectOutcome {
  id: number;
  description: string;
  rubric: Rubric | null;
}

export interface Rubric {
  id: number;
  description: string;
  subjectOutcome: SubjectOutcome | null;
  criteria: Criterion[];
}

export interface Criterion {
  id: number;
  weight: number;
  name: string;
  rubric: Rubric | null;
  levels: Level[];
}

export interface Level {
  id: number;
  category: string;
  description: string;
  percentage: number;
  criterion: Criterion | null;
}

export interface Term {
  id: number;
  description: string;
}

export interface ErrorResponse {
  error: string;
  message: string;
}
```

---

**Última actualización:** 23 de mayo de 2025
