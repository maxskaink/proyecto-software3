-- Script de carga de datos por defecto con validación para CoreService
-- Este script crea competencias de programa, resultados de programa, competencias de asignatura,
-- asignaciones de competencias, resultados de asignatura, rúbricas, criterios y niveles para dos periodos
-- Respetando la estructura de la base de datos y las relaciones entre entidades

-- 1. Insertar periodos académicos si no existen
INSERT INTO periodo (term)
SELECT '2024-2' WHERE NOT EXISTS (SELECT 1 FROM periodo WHERE term = '2024-2');
INSERT INTO periodo (term)
SELECT '2025-1' WHERE NOT EXISTS (SELECT 1 FROM periodo WHERE term = '2025-1');

-- 2. Asegurarnos que existe la configuración con el periodo activo
INSERT INTO configuracion (periodo_actual_id)
SELECT 1 WHERE NOT EXISTS (SELECT 1 FROM configuracion);

-- 3. Insertar asignaturas
INSERT INTO asignatura (name, description, is_activated)
SELECT 'Programación I', 'Fundamentos de programación en Java', true 
WHERE NOT EXISTS (SELECT 1 FROM asignatura WHERE name = 'Programación I');
INSERT INTO asignatura (name, description, is_activated)
SELECT 'Estructuras de Datos', 'Uso de estructuras básicas como listas, pilas, colas y árboles', true 
WHERE NOT EXISTS (SELECT 1 FROM asignatura WHERE name = 'Estructuras de Datos');
INSERT INTO asignatura (name, description, is_activated)
SELECT 'Algoritmos', 'Diseño y análisis de algoritmos', true 
WHERE NOT EXISTS (SELECT 1 FROM asignatura WHERE name = 'Algoritmos');

-- 4. Asignar docente a todas las asignaturas en todos los periodos
-- Nota: id_docente es un string UID, no una referencia a una tabla docente
INSERT INTO asignacion_docente (id_docente, id_asignatura, id_periodo)
SELECT 'aAy1Qjjjk2glflMYDlVrAHY9ZdD3', a.id, p.id
FROM asignatura a, periodo p
WHERE p.term IN ('2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM asignacion_docente 
  WHERE id_docente = 'aAy1Qjjjk2glflMYDlVrAHY9ZdD3' AND id_asignatura = a.id AND id_periodo = p.id
);

-- 5. Insertar competencias de programa
INSERT INTO competencia_programa (description, level, is_activated)
SELECT 'Competencia 1: Programación básica', 'Básico', true
WHERE NOT EXISTS (SELECT 1 FROM competencia_programa WHERE description = 'Competencia 1: Programación básica');

INSERT INTO competencia_programa (description, level, is_activated)
SELECT 'Competencia 2: Estructuras de datos', 'Intermedio', true
WHERE NOT EXISTS (SELECT 1 FROM competencia_programa WHERE description = 'Competencia 2: Estructuras de datos');

-- 6. Insertar resultados de aprendizaje de programa (RA) asociados a competencias
INSERT INTO ra_programa (description, id_competencia, is_activated)
SELECT 'RA1: Aplica conceptos básicos de programación', 
       (SELECT id FROM competencia_programa WHERE description = 'Competencia 1: Programación básica'), 
       true 
WHERE NOT EXISTS (SELECT 1 FROM ra_programa WHERE description = 'RA1: Aplica conceptos básicos de programación');
INSERT INTO ra_programa (description, id_competencia, is_activated)
SELECT 'RA2: Utiliza estructuras de datos', 
       (SELECT id FROM competencia_programa WHERE description = 'Competencia 2: Estructuras de datos'), 
       true 
WHERE NOT EXISTS (SELECT 1 FROM ra_programa WHERE description = 'RA2: Utiliza estructuras de datos');

-- 7. Crear competencias de asignatura (relacionadas a competencias de programa)
-- Competencias para Programación I
INSERT INTO competencia_asignatura (description, id_competencia_peograma, is_activated)
SELECT 'Desarrollar soluciones algorítmicas básicas utilizando Java', 
       (SELECT id FROM competencia_programa WHERE description = 'Competencia 1: Programación básica'), 
       true
WHERE NOT EXISTS (
  SELECT 1 FROM competencia_asignatura 
  WHERE description = 'Desarrollar soluciones algorítmicas básicas utilizando Java'
);

-- Competencias para Estructuras de Datos
INSERT INTO competencia_asignatura (description, id_competencia_peograma, is_activated)
SELECT 'Implementar y manipular estructuras de datos fundamentales', 
       (SELECT id FROM competencia_programa WHERE description = 'Competencia 2: Estructuras de datos'), 
       true
WHERE NOT EXISTS (
  SELECT 1 FROM competencia_asignatura 
  WHERE description = 'Implementar y manipular estructuras de datos fundamentales'
);

-- Competencias para Algoritmos relacionada con programación básica
INSERT INTO competencia_asignatura (description, id_competencia_peograma, is_activated)
SELECT 'Analizar la eficiencia de algoritmos de programación', 
       (SELECT id FROM competencia_programa WHERE description = 'Competencia 1: Programación básica'), 
       true
WHERE NOT EXISTS (
  SELECT 1 FROM competencia_asignatura 
  WHERE description = 'Analizar la eficiencia de algoritmos de programación'
);

-- 8. Asignar competencias de asignatura a asignaturas en periodos específicos
-- Asignar competencia de programación básica a Programación I
INSERT INTO asignacion_competencia_asignatura (id_competencia, id_asignatura, id_periodo, is_activated)
SELECT 
    (SELECT id FROM competencia_asignatura WHERE description = 'Desarrollar soluciones algorítmicas básicas utilizando Java'),
    (SELECT id FROM asignatura WHERE name = 'Programación I'),
    p.id,
    true
FROM periodo p
WHERE p.term IN ('2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM asignacion_competencia_asignatura 
  WHERE id_competencia = (SELECT id FROM competencia_asignatura WHERE description = 'Desarrollar soluciones algorítmicas básicas utilizando Java')
  AND id_asignatura = (SELECT id FROM asignatura WHERE name = 'Programación I')
  AND id_periodo = p.id
);

-- Asignar competencia de estructuras de datos a la asignatura Estructuras de Datos
INSERT INTO asignacion_competencia_asignatura (id_competencia, id_asignatura, id_periodo, is_activated)
SELECT 
    (SELECT id FROM competencia_asignatura WHERE description = 'Implementar y manipular estructuras de datos fundamentales'),
    (SELECT id FROM asignatura WHERE name = 'Estructuras de Datos'),
    p.id,
    true
FROM periodo p
WHERE p.term IN ('2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM asignacion_competencia_asignatura 
  WHERE id_competencia = (SELECT id FROM competencia_asignatura WHERE description = 'Implementar y manipular estructuras de datos fundamentales')
  AND id_asignatura = (SELECT id FROM asignatura WHERE name = 'Estructuras de Datos')
  AND id_periodo = p.id
);

-- Asignar competencia de análisis de algoritmos a la asignatura Algoritmos
INSERT INTO asignacion_competencia_asignatura (id_competencia, id_asignatura, id_periodo, is_activated)
SELECT 
    (SELECT id FROM competencia_asignatura WHERE description = 'Analizar la eficiencia de algoritmos de programación'),
    (SELECT id FROM asignatura WHERE name = 'Algoritmos'),
    p.id,
    true
FROM periodo p
WHERE p.term IN ('2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM asignacion_competencia_asignatura 
  WHERE id_competencia = (SELECT id FROM competencia_asignatura WHERE description = 'Analizar la eficiencia de algoritmos de programación')
  AND id_asignatura = (SELECT id FROM asignatura WHERE name = 'Algoritmos')
  AND id_periodo = p.id
);

-- 9. Insertar resultados de aprendizaje de asignatura para cada asignación de competencia
-- RA para Programación I
INSERT INTO ra_asignatura (description, id_competencia, is_activated)
SELECT 'Implementar algoritmos básicos en Java usando estructuras de control', 
       (SELECT aca.id FROM asignacion_competencia_asignatura aca
       JOIN competencia_asignatura ca ON aca.id_competencia = ca.id
       WHERE ca.description = 'Desarrollar soluciones algorítmicas básicas utilizando Java'
       LIMIT 1), 
       true
WHERE NOT EXISTS (
  SELECT 1 FROM ra_asignatura 
  WHERE description = 'Implementar algoritmos básicos en Java usando estructuras de control'
);

INSERT INTO ra_asignatura (description, id_competencia, is_activated)
SELECT 'Aplicar principios de programación orientada a objetos en contextos simples', 
       (SELECT aca.id FROM asignacion_competencia_asignatura aca
       JOIN competencia_asignatura ca ON aca.id_competencia = ca.id
       WHERE ca.description = 'Desarrollar soluciones algorítmicas básicas utilizando Java'
       LIMIT 1), 
       true
WHERE NOT EXISTS (
  SELECT 1 FROM ra_asignatura 
  WHERE description = 'Aplicar principios de programación orientada a objetos en contextos simples'
);

-- RA para Estructuras de Datos
INSERT INTO ra_asignatura (description, id_competencia, is_activated)
SELECT 'Implementar estructuras de datos lineales (listas, pilas, colas)', 
       (SELECT aca.id FROM asignacion_competencia_asignatura aca
       JOIN competencia_asignatura ca ON aca.id_competencia = ca.id
       WHERE ca.description = 'Implementar y manipular estructuras de datos fundamentales'
       LIMIT 1), 
       true
WHERE NOT EXISTS (
  SELECT 1 FROM ra_asignatura 
  WHERE description = 'Implementar estructuras de datos lineales (listas, pilas, colas)'
);

INSERT INTO ra_asignatura (description, id_competencia, is_activated)
SELECT 'Implementar estructuras de datos jerárquicas (árboles, grafos)', 
       (SELECT aca.id FROM asignacion_competencia_asignatura aca
       JOIN competencia_asignatura ca ON aca.id_competencia = ca.id
       WHERE ca.description = 'Implementar y manipular estructuras de datos fundamentales'
       LIMIT 1), 
       true
WHERE NOT EXISTS (
  SELECT 1 FROM ra_asignatura 
  WHERE description = 'Implementar estructuras de datos jerárquicas (árboles, grafos)'
);

-- RA para Algoritmos
INSERT INTO ra_asignatura (description, id_competencia, is_activated)
SELECT 'Analizar la complejidad temporal y espacial de algoritmos', 
       (SELECT aca.id FROM asignacion_competencia_asignatura aca
       JOIN competencia_asignatura ca ON aca.id_competencia = ca.id
       WHERE ca.description = 'Analizar la eficiencia de algoritmos de programación'
       LIMIT 1), 
       true
WHERE NOT EXISTS (
  SELECT 1 FROM ra_asignatura 
  WHERE description = 'Analizar la complejidad temporal y espacial de algoritmos'
);

INSERT INTO ra_asignatura (description, id_competencia, is_activated)
SELECT 'Diseñar algoritmos eficientes para problemas complejos', 
       (SELECT aca.id FROM asignacion_competencia_asignatura aca
       JOIN competencia_asignatura ca ON aca.id_competencia = ca.id
       WHERE ca.description = 'Analizar la eficiencia de algoritmos de programación'
       LIMIT 1), 
       true
WHERE NOT EXISTS (
  SELECT 1 FROM ra_asignatura 
  WHERE description = 'Diseñar algoritmos eficientes para problemas complejos'
);

-- 10. Insertar rúbricas para cada resultado de aprendizaje de asignatura y actualizar la referencia bidireccional
-- Rúbrica para RA de Programación I: Implementar algoritmos básicos
INSERT INTO rubrica (description, subject_outcome_id, is_activated)
SELECT 'Rúbrica para evaluación de implementación de algoritmos', 
       ra.id, 
       true
FROM ra_asignatura ra 
WHERE ra.description = 'Implementar algoritmos básicos en Java usando estructuras de control'
AND NOT EXISTS (
  SELECT 1 FROM rubrica 
  WHERE subject_outcome_id = ra.id
);

-- Actualizar la referencia desde RA a Rúbrica para RA de Programación I: Implementar algoritmos
UPDATE ra_asignatura
SET rubric_id = (
  SELECT r.id FROM rubrica r 
  WHERE r.subject_outcome_id = ra_asignatura.id 
  AND r.description = 'Rúbrica para evaluación de implementación de algoritmos'
)
WHERE description = 'Implementar algoritmos básicos en Java usando estructuras de control'
AND NOT EXISTS (SELECT 1 FROM ra_asignatura WHERE description = 'Implementar algoritmos básicos en Java usando estructuras de control' AND rubric_id IS NOT NULL);

-- Rúbrica para RA de Programación I: Aplicar POO
INSERT INTO rubrica (description, subject_outcome_id, is_activated)
SELECT 'Rúbrica para evaluación de programación orientada a objetos', 
       ra.id, 
       true
FROM ra_asignatura ra 
WHERE ra.description = 'Aplicar principios de programación orientada a objetos en contextos simples'
AND NOT EXISTS (
  SELECT 1 FROM rubrica 
  WHERE subject_outcome_id = ra.id
);

-- Actualizar la referencia desde RA a Rúbrica para RA de Programación I: Aplicar POO
UPDATE ra_asignatura
SET rubric_id = (
  SELECT r.id FROM rubrica r 
  WHERE r.subject_outcome_id = ra_asignatura.id 
  AND r.description = 'Rúbrica para evaluación de programación orientada a objetos'
)
WHERE description = 'Aplicar principios de programación orientada a objetos en contextos simples'
AND NOT EXISTS (SELECT 1 FROM ra_asignatura WHERE description = 'Aplicar principios de programación orientada a objetos en contextos simples' AND rubric_id IS NOT NULL);

-- Rúbrica para RA de Estructuras de Datos: Estructuras lineales
INSERT INTO rubrica (description, subject_outcome_id, is_activated)
SELECT 'Rúbrica para evaluación de estructuras de datos lineales', 
       ra.id, 
       true
FROM ra_asignatura ra 
WHERE ra.description = 'Implementar estructuras de datos lineales (listas, pilas, colas)'
AND NOT EXISTS (
  SELECT 1 FROM rubrica 
  WHERE subject_outcome_id = ra.id
);

-- Actualizar la referencia desde RA a Rúbrica para RA de Estructuras de Datos: Estructuras lineales
UPDATE ra_asignatura
SET rubric_id = (
  SELECT r.id FROM rubrica r 
  WHERE r.subject_outcome_id = ra_asignatura.id 
  AND r.description = 'Rúbrica para evaluación de estructuras de datos lineales'
)
WHERE description = 'Implementar estructuras de datos lineales (listas, pilas, colas)'
AND NOT EXISTS (SELECT 1 FROM ra_asignatura WHERE description = 'Implementar estructuras de datos lineales (listas, pilas, colas)' AND rubric_id IS NOT NULL);

-- Rúbrica para RA de Estructuras de Datos: Estructuras jerárquicas
INSERT INTO rubrica (description, subject_outcome_id, is_activated)
SELECT 'Rúbrica para evaluación de estructuras de datos jerárquicas', 
       ra.id, 
       true
FROM ra_asignatura ra 
WHERE ra.description = 'Implementar estructuras de datos jerárquicas (árboles, grafos)'
AND NOT EXISTS (
  SELECT 1 FROM rubrica 
  WHERE subject_outcome_id = ra.id
);

-- Actualizar la referencia desde RA a Rúbrica para RA de Estructuras de Datos: Estructuras jerárquicas
UPDATE ra_asignatura
SET rubric_id = (
  SELECT r.id FROM rubrica r 
  WHERE r.subject_outcome_id = ra_asignatura.id 
  AND r.description = 'Rúbrica para evaluación de estructuras de datos jerárquicas'
)
WHERE description = 'Implementar estructuras de datos jerárquicas (árboles, grafos)'
AND NOT EXISTS (SELECT 1 FROM ra_asignatura WHERE description = 'Implementar estructuras de datos jerárquicas (árboles, grafos)' AND rubric_id IS NOT NULL);

-- Rúbrica para RA de Algoritmos: Análisis de complejidad
INSERT INTO rubrica (description, subject_outcome_id, is_activated)
SELECT 'Rúbrica para evaluación de análisis de complejidad algorítmica', 
       ra.id, 
       true
FROM ra_asignatura ra 
WHERE ra.description = 'Analizar la complejidad temporal y espacial de algoritmos'
AND NOT EXISTS (
  SELECT 1 FROM rubrica 
  WHERE subject_outcome_id = ra.id
);

-- Actualizar la referencia desde RA a Rúbrica para RA de Algoritmos: Análisis de complejidad
UPDATE ra_asignatura
SET rubric_id = (
  SELECT r.id FROM rubrica r 
  WHERE r.subject_outcome_id = ra_asignatura.id 
  AND r.description = 'Rúbrica para evaluación de análisis de complejidad algorítmica'
)
WHERE description = 'Analizar la complejidad temporal y espacial de algoritmos'
AND NOT EXISTS (SELECT 1 FROM ra_asignatura WHERE description = 'Analizar la complejidad temporal y espacial de algoritmos' AND rubric_id IS NOT NULL);

-- Rúbrica para RA de Algoritmos: Diseño de algoritmos
INSERT INTO rubrica (description, subject_outcome_id, is_activated)
SELECT 'Rúbrica para evaluación de diseño de algoritmos eficientes', 
       ra.id, 
       true
FROM ra_asignatura ra 
WHERE ra.description = 'Diseñar algoritmos eficientes para problemas complejos'
AND NOT EXISTS (
  SELECT 1 FROM rubrica 
  WHERE subject_outcome_id = ra.id
);

-- Actualizar la referencia desde RA a Rúbrica para RA de Algoritmos: Diseño de algoritmos
UPDATE ra_asignatura
SET rubric_id = (
  SELECT r.id FROM rubrica r 
  WHERE r.subject_outcome_id = ra_asignatura.id 
  AND r.description = 'Rúbrica para evaluación de diseño de algoritmos eficientes'
)
WHERE description = 'Diseñar algoritmos eficientes para problemas complejos'
AND NOT EXISTS (SELECT 1 FROM ra_asignatura WHERE description = 'Diseñar algoritmos eficientes para problemas complejos' AND rubric_id IS NOT NULL);

-- 11. Insertar criterios para cada rúbrica
-- Criterios para rúbrica de implementación de algoritmos
INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Corrección funcional', 60, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de implementación de algoritmos'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Corrección funcional'
);

INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Legibilidad del código', 40, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de implementación de algoritmos'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Legibilidad del código'
);

-- Criterios para rúbrica de programación orientada a objetos
INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Diseño de clases', 50, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de programación orientada a objetos'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Diseño de clases'
);

INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Uso de encapsulamiento', 50, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de programación orientada a objetos'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Uso de encapsulamiento'
);

-- Criterios para rúbrica de estructuras de datos lineales
INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Implementación correcta', 70, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de estructuras de datos lineales'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Implementación correcta'
);

INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Manejo de casos borde', 30, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de estructuras de datos lineales'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Manejo de casos borde'
);

-- Criterios para rúbrica de estructuras de datos jerárquicas
INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Implementación de operaciones', 60, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de estructuras de datos jerárquicas'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Implementación de operaciones'
);

INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Eficiencia de la implementación', 40, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de estructuras de datos jerárquicas'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Eficiencia de la implementación'
);

-- Criterios para rúbrica de análisis de complejidad
INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Análisis matemático', 50, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de análisis de complejidad algorítmica'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Análisis matemático'
);

INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Identificación de cuellos de botella', 50, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de análisis de complejidad algorítmica'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Identificación de cuellos de botella'
);

-- Criterios para rúbrica de diseño de algoritmos
INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Elección de estrategia algorítmica', 60, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de diseño de algoritmos eficientes'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Elección de estrategia algorítmica'
);

INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Optimización de recursos', 40, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de diseño de algoritmos eficientes'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Optimización de recursos'
);
-- 12. Insertar niveles para cada criterio
-- Nivel Excelente
INSERT INTO nivel (category, description, percentage, id_criterio)
SELECT 'Excelente', 'Nivel excelente', 100, c.id
FROM criterio c
WHERE NOT EXISTS (
  SELECT 1 FROM nivel 
  WHERE id_criterio = c.id AND category = 'Excelente'
);

-- Nivel Bueno
INSERT INTO nivel (category, description, percentage, id_criterio)
SELECT 'Bueno', 'Nivel bueno', 80, c.id
FROM criterio c
WHERE NOT EXISTS (
  SELECT 1 FROM nivel 
  WHERE id_criterio = c.id AND category = 'Bueno'
);

-- Nivel Básico
INSERT INTO nivel (category, description, percentage, id_criterio)
SELECT 'Básico', 'Nivel básico', 60, c.id
FROM criterio c
WHERE NOT EXISTS (
  SELECT 1 FROM nivel 
  WHERE id_criterio = c.id AND category = 'Básico'
);

-- Fin del script de carga por defecto
