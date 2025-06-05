-- Script de carga de datos por defecto con validación para CoreService
-- Este script crea competencias de programa, resultados de programa, competencias de asignatura,
-- asignaciones de competencias, resultados de asignatura, rúbricas, criterios y niveles para dos periodos
-- Respetando la estructura de la base de datos y las relaciones entre entidades

-- 1. Insertar periodos académicos si no existen
INSERT INTO periodo (term)
SELECT '2024-1' WHERE NOT EXISTS (SELECT 1 FROM periodo WHERE term = '2024-1');
INSERT INTO periodo (term)
SELECT '2024-2' WHERE NOT EXISTS (SELECT 1 FROM periodo WHERE term = '2024-2');
INSERT INTO periodo (term)
SELECT '2025-1' WHERE NOT EXISTS (SELECT 1 FROM periodo WHERE term = '2025-1');

-- 2. Asegurarnos que existe la configuración con el periodo activo
INSERT INTO configuracion (periodo_actual_id)
SELECT 1 WHERE NOT EXISTS (SELECT 1 FROM configuracion);

-- Comentario especial para el periodo 2024-1
-- Este es el primer periodo académico del año 2024, que contiene configuraciones iniciales
-- y servirá como base para comparar el progreso en los periodos siguientes.

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
WHERE p.term IN ('2024-1', '2024-2', '2025-1')
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
-- CADA ASIGNATURA DEBE TENER AL MENOS DOS COMPETENCIAS

-- Primera competencia para Programación I
INSERT INTO competencia_asignatura (description, id_competencia_peograma, is_activated)
SELECT 'Desarrollar soluciones algorítmicas básicas utilizando Java', 
       (SELECT id FROM competencia_programa WHERE description = 'Competencia 1: Programación básica'), 
       true
WHERE NOT EXISTS (
  SELECT 1 FROM competencia_asignatura 
  WHERE description = 'Desarrollar soluciones algorítmicas básicas utilizando Java'
);

-- Segunda competencia para Programación I
INSERT INTO competencia_asignatura (description, id_competencia_peograma, is_activated)
SELECT 'Aplicar principios de programación orientada a objetos en Java', 
       (SELECT id FROM competencia_programa WHERE description = 'Competencia 1: Programación básica'), 
       true
WHERE NOT EXISTS (
  SELECT 1 FROM competencia_asignatura 
  WHERE description = 'Aplicar principios de programación orientada a objetos en Java'
);

-- Primera competencia para Estructuras de Datos
INSERT INTO competencia_asignatura (description, id_competencia_peograma, is_activated)
SELECT 'Implementar y manipular estructuras de datos fundamentales', 
       (SELECT id FROM competencia_programa WHERE description = 'Competencia 2: Estructuras de datos'), 
       true
WHERE NOT EXISTS (
  SELECT 1 FROM competencia_asignatura 
  WHERE description = 'Implementar y manipular estructuras de datos fundamentales'
);

-- Segunda competencia para Estructuras de Datos
INSERT INTO competencia_asignatura (description, id_competencia_peograma, is_activated)
SELECT 'Analizar la eficiencia de estructuras de datos en diferentes contextos', 
       (SELECT id FROM competencia_programa WHERE description = 'Competencia 2: Estructuras de datos'), 
       true
WHERE NOT EXISTS (
  SELECT 1 FROM competencia_asignatura 
  WHERE description = 'Analizar la eficiencia de estructuras de datos en diferentes contextos'
);

-- Primera competencia para Algoritmos
INSERT INTO competencia_asignatura (description, id_competencia_peograma, is_activated)
SELECT 'Analizar la eficiencia de algoritmos de programación', 
       (SELECT id FROM competencia_programa WHERE description = 'Competencia 1: Programación básica'), 
       true
WHERE NOT EXISTS (
  SELECT 1 FROM competencia_asignatura 
  WHERE description = 'Analizar la eficiencia de algoritmos de programación'
);

-- Segunda competencia para Algoritmos
INSERT INTO competencia_asignatura (description, id_competencia_peograma, is_activated)
SELECT 'Diseñar algoritmos optimizados para resolver problemas complejos', 
       (SELECT id FROM competencia_programa WHERE description = 'Competencia 1: Programación básica'), 
       true
WHERE NOT EXISTS (
  SELECT 1 FROM competencia_asignatura 
  WHERE description = 'Diseñar algoritmos optimizados para resolver problemas complejos'
);

-- 8. Asignar competencias de asignatura a asignaturas en periodos específicos
-- Asignar PRIMERA competencia de programación básica a Programación I
INSERT INTO asignacion_competencia_asignatura (id_competencia, id_asignatura, id_periodo, is_activated)
SELECT 
    (SELECT id FROM competencia_asignatura WHERE description = 'Desarrollar soluciones algorítmicas básicas utilizando Java'),
    (SELECT id FROM asignatura WHERE name = 'Programación I'),
    p.id,
    true
FROM periodo p
WHERE p.term IN ('2024-1', '2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM asignacion_competencia_asignatura 
  WHERE id_competencia = (SELECT id FROM competencia_asignatura WHERE description = 'Desarrollar soluciones algorítmicas básicas utilizando Java')
  AND id_asignatura = (SELECT id FROM asignatura WHERE name = 'Programación I')
  AND id_periodo = p.id
);

-- Asignar SEGUNDA competencia de programación orientada a objetos a Programación I
INSERT INTO asignacion_competencia_asignatura (id_competencia, id_asignatura, id_periodo, is_activated)
SELECT 
    (SELECT id FROM competencia_asignatura WHERE description = 'Aplicar principios de programación orientada a objetos en Java'),
    (SELECT id FROM asignatura WHERE name = 'Programación I'),
    p.id,
    true
FROM periodo p
WHERE p.term IN ('2024-1', '2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM asignacion_competencia_asignatura 
  WHERE id_competencia = (SELECT id FROM competencia_asignatura WHERE description = 'Aplicar principios de programación orientada a objetos en Java')
  AND id_asignatura = (SELECT id FROM asignatura WHERE name = 'Programación I')
  AND id_periodo = p.id
);

-- Asignar PRIMERA competencia de estructuras de datos a la asignatura Estructuras de Datos
INSERT INTO asignacion_competencia_asignatura (id_competencia, id_asignatura, id_periodo, is_activated)
SELECT 
    (SELECT id FROM competencia_asignatura WHERE description = 'Implementar y manipular estructuras de datos fundamentales'),
    (SELECT id FROM asignatura WHERE name = 'Estructuras de Datos'),
    p.id,
    true
FROM periodo p
WHERE p.term IN ('2024-1', '2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM asignacion_competencia_asignatura 
  WHERE id_competencia = (SELECT id FROM competencia_asignatura WHERE description = 'Implementar y manipular estructuras de datos fundamentales')
  AND id_asignatura = (SELECT id FROM asignatura WHERE name = 'Estructuras de Datos')
  AND id_periodo = p.id
);

-- Asignar SEGUNDA competencia de análisis de eficiencia a la asignatura Estructuras de Datos
INSERT INTO asignacion_competencia_asignatura (id_competencia, id_asignatura, id_periodo, is_activated)
SELECT 
    (SELECT id FROM competencia_asignatura WHERE description = 'Analizar la eficiencia de estructuras de datos en diferentes contextos'),
    (SELECT id FROM asignatura WHERE name = 'Estructuras de Datos'),
    p.id,
    true
FROM periodo p
WHERE p.term IN ('2024-1', '2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM asignacion_competencia_asignatura 
  WHERE id_competencia = (SELECT id FROM competencia_asignatura WHERE description = 'Analizar la eficiencia de estructuras de datos en diferentes contextos')
  AND id_asignatura = (SELECT id FROM asignatura WHERE name = 'Estructuras de Datos')
  AND id_periodo = p.id
);

-- Asignar PRIMERA competencia de análisis de algoritmos a la asignatura Algoritmos
INSERT INTO asignacion_competencia_asignatura (id_competencia, id_asignatura, id_periodo, is_activated)
SELECT 
    (SELECT id FROM competencia_asignatura WHERE description = 'Analizar la eficiencia de algoritmos de programación'),
    (SELECT id FROM asignatura WHERE name = 'Algoritmos'),
    p.id,
    true
FROM periodo p
WHERE p.term IN ('2024-1', '2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM asignacion_competencia_asignatura 
  WHERE id_competencia = (SELECT id FROM competencia_asignatura WHERE description = 'Analizar la eficiencia de algoritmos de programación')
  AND id_asignatura = (SELECT id FROM asignatura WHERE name = 'Algoritmos')
  AND id_periodo = p.id
);

-- Asignar SEGUNDA competencia de diseño de algoritmos a la asignatura Algoritmos
INSERT INTO asignacion_competencia_asignatura (id_competencia, id_asignatura, id_periodo, is_activated)
SELECT 
    (SELECT id FROM competencia_asignatura WHERE description = 'Diseñar algoritmos optimizados para resolver problemas complejos'),
    (SELECT id FROM asignatura WHERE name = 'Algoritmos'),
    p.id,
    true
FROM periodo p
WHERE p.term IN ('2024-1', '2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM asignacion_competencia_asignatura 
  WHERE id_competencia = (SELECT id FROM competencia_asignatura WHERE description = 'Diseñar algoritmos optimizados para resolver problemas complejos')
  AND id_asignatura = (SELECT id FROM asignatura WHERE name = 'Algoritmos')
  AND id_periodo = p.id
);

-- 9. Insertar resultados de aprendizaje de asignatura para cada asignación de competencia
-- Cada competencia debe tener AL MENOS DOS RA

-- RAs para Programación I - Primera competencia (Desarrollar soluciones algorítmicas básicas)
INSERT INTO ra_asignatura (description, id_competencia, is_activated)
SELECT DISTINCT 'Implementar algoritmos básicos en Java usando estructuras de control', 
       aca.id, 
       true
FROM asignacion_competencia_asignatura aca
JOIN competencia_asignatura ca ON aca.id_competencia = ca.id
JOIN periodo p ON aca.id_periodo = p.id
WHERE ca.description = 'Desarrollar soluciones algorítmicas básicas utilizando Java'
AND p.term IN ('2024-1', '2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM ra_asignatura ra 
  WHERE ra.description = 'Implementar algoritmos básicos en Java usando estructuras de control'
  AND ra.id_competencia = aca.id
);

INSERT INTO ra_asignatura (description, id_competencia, is_activated)
SELECT DISTINCT 'Resolver problemas computacionales mediante algoritmos secuenciales', 
       aca.id, 
       true
FROM asignacion_competencia_asignatura aca
JOIN competencia_asignatura ca ON aca.id_competencia = ca.id
JOIN periodo p ON aca.id_periodo = p.id
WHERE ca.description = 'Desarrollar soluciones algorítmicas básicas utilizando Java'
AND p.term IN ('2024-1', '2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM ra_asignatura ra
  WHERE ra.description = 'Resolver problemas computacionales mediante algoritmos secuenciales'
  AND ra.id_competencia = aca.id
);

-- RAs para Programación I - Segunda competencia (Aplicar principios de POO)
INSERT INTO ra_asignatura (description, id_competencia, is_activated)
SELECT DISTINCT 'Diseñar clases y objetos siguiendo principios de encapsulamiento', 
       aca.id, 
       true
FROM asignacion_competencia_asignatura aca
JOIN competencia_asignatura ca ON aca.id_competencia = ca.id
JOIN periodo p ON aca.id_periodo = p.id
WHERE ca.description = 'Aplicar principios de programación orientada a objetos en Java'
AND p.term IN ('2024-1', '2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM ra_asignatura ra
  WHERE ra.description = 'Diseñar clases y objetos siguiendo principios de encapsulamiento'
  AND ra.id_competencia = aca.id
);

INSERT INTO ra_asignatura (description, id_competencia, is_activated)
SELECT DISTINCT 'Implementar herencia y polimorfismo en soluciones de software', 
       aca.id, 
       true
FROM asignacion_competencia_asignatura aca
JOIN competencia_asignatura ca ON aca.id_competencia = ca.id
JOIN periodo p ON aca.id_periodo = p.id
WHERE ca.description = 'Aplicar principios de programación orientada a objetos en Java'
AND p.term IN ('2024-1', '2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM ra_asignatura ra
  WHERE ra.description = 'Implementar herencia y polimorfismo en soluciones de software'
  AND ra.id_competencia = aca.id
);

-- RAs para Estructuras de Datos - Primera competencia (Implementar y manipular estructuras)
INSERT INTO ra_asignatura (description, id_competencia, is_activated)
SELECT DISTINCT 'Implementar estructuras de datos lineales (listas, pilas, colas)', 
       aca.id, 
       true
FROM asignacion_competencia_asignatura aca
JOIN competencia_asignatura ca ON aca.id_competencia = ca.id
JOIN periodo p ON aca.id_periodo = p.id
WHERE ca.description = 'Implementar y manipular estructuras de datos fundamentales'
AND p.term IN ('2024-1', '2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM ra_asignatura ra
  WHERE ra.description = 'Implementar estructuras de datos lineales (listas, pilas, colas)'
  AND ra.id_competencia = aca.id
);

INSERT INTO ra_asignatura (description, id_competencia, is_activated)
SELECT DISTINCT 'Implementar estructuras de datos jerárquicas (árboles, grafos)', 
       aca.id, 
       true
FROM asignacion_competencia_asignatura aca
JOIN competencia_asignatura ca ON aca.id_competencia = ca.id
JOIN periodo p ON aca.id_periodo = p.id
WHERE ca.description = 'Implementar y manipular estructuras de datos fundamentales'
AND p.term IN ('2024-1', '2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM ra_asignatura ra
  WHERE ra.description = 'Implementar estructuras de datos jerárquicas (árboles, grafos)'
  AND ra.id_competencia = aca.id
);

-- RAs para Estructuras de Datos - Segunda competencia (Analizar eficiencia)
INSERT INTO ra_asignatura (description, id_competencia, is_activated)
SELECT DISTINCT 'Evaluar la complejidad temporal de estructuras de datos', 
       aca.id, 
       true
FROM asignacion_competencia_asignatura aca
JOIN competencia_asignatura ca ON aca.id_competencia = ca.id
JOIN periodo p ON aca.id_periodo = p.id
WHERE ca.description = 'Analizar la eficiencia de estructuras de datos en diferentes contextos'
AND p.term IN ('2024-1', '2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM ra_asignatura ra
  WHERE ra.description = 'Evaluar la complejidad temporal de estructuras de datos'
  AND ra.id_competencia = aca.id
);

INSERT INTO ra_asignatura (description, id_competencia, is_activated)
SELECT DISTINCT 'Comparar el rendimiento de diferentes estructuras de datos', 
       aca.id, 
       true
FROM asignacion_competencia_asignatura aca
JOIN competencia_asignatura ca ON aca.id_competencia = ca.id
JOIN periodo p ON aca.id_periodo = p.id
WHERE ca.description = 'Analizar la eficiencia de estructuras de datos en diferentes contextos'
AND p.term IN ('2024-1', '2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM ra_asignatura ra
  WHERE ra.description = 'Comparar el rendimiento de diferentes estructuras de datos'
  AND ra.id_competencia = aca.id
);

-- RAs para Algoritmos - Primera competencia (Analizar eficiencia de algoritmos)
INSERT INTO ra_asignatura (description, id_competencia, is_activated)
SELECT DISTINCT 'Analizar la complejidad temporal y espacial de algoritmos', 
       aca.id, 
       true
FROM asignacion_competencia_asignatura aca
JOIN competencia_asignatura ca ON aca.id_competencia = ca.id
JOIN periodo p ON aca.id_periodo = p.id
WHERE ca.description = 'Analizar la eficiencia de algoritmos de programación'
AND p.term IN ('2024-1', '2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM ra_asignatura ra
  WHERE ra.description = 'Analizar la complejidad temporal y espacial de algoritmos'
  AND ra.id_competencia = aca.id
);

INSERT INTO ra_asignatura (description, id_competencia, is_activated)
SELECT DISTINCT 'Aplicar técnicas de análisis asintótico en algoritmos', 
       aca.id, 
       true
FROM asignacion_competencia_asignatura aca
JOIN competencia_asignatura ca ON aca.id_competencia = ca.id
JOIN periodo p ON aca.id_periodo = p.id
WHERE ca.description = 'Analizar la eficiencia de algoritmos de programación'
AND p.term IN ('2024-1', '2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM ra_asignatura ra
  WHERE ra.description = 'Aplicar técnicas de análisis asintótico en algoritmos'
  AND ra.id_competencia = aca.id
);

-- RAs para Algoritmos - Segunda competencia (Diseñar algoritmos optimizados)
INSERT INTO ra_asignatura (description, id_competencia, is_activated)
SELECT DISTINCT 'Diseñar algoritmos eficientes para problemas complejos', 
       aca.id, 
       true
FROM asignacion_competencia_asignatura aca
JOIN competencia_asignatura ca ON aca.id_competencia = ca.id
JOIN periodo p ON aca.id_periodo = p.id
WHERE ca.description = 'Diseñar algoritmos optimizados para resolver problemas complejos'
AND p.term IN ('2024-1', '2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM ra_asignatura ra
  WHERE ra.description = 'Diseñar algoritmos eficientes para problemas complejos'
  AND ra.id_competencia = aca.id
);

INSERT INTO ra_asignatura (description, id_competencia, is_activated)
SELECT DISTINCT 'Optimizar algoritmos existentes mediante técnicas avanzadas', 
       aca.id, 
       true
FROM asignacion_competencia_asignatura aca
JOIN competencia_asignatura ca ON aca.id_competencia = ca.id
JOIN periodo p ON aca.id_periodo = p.id
WHERE ca.description = 'Diseñar algoritmos optimizados para resolver problemas complejos'
AND p.term IN ('2024-1', '2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM ra_asignatura ra
  WHERE ra.description = 'Optimizar algoritmos existentes mediante técnicas avanzadas'
  AND ra.id_competencia = aca.id
);

-- 10. Insertar rúbricas para cada resultado de aprendizaje de asignatura y actualizar la referencia bidireccional
-- Rúbrica para RA de Programación I: Implementar algoritmos básicos
INSERT INTO rubrica (description, subject_outcome_id, is_activated)
SELECT 'Rúbrica para evaluación de implementación de algoritmos', 
       ra.id, 
       true
FROM ra_asignatura ra 
JOIN asignacion_competencia_asignatura aca ON ra.id_competencia = aca.id
JOIN periodo p ON aca.id_periodo = p.id
WHERE ra.description = 'Implementar algoritmos básicos en Java usando estructuras de control'
AND p.term IN ('2024-1', '2024-2', '2025-1')
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
AND rubric_id IS NULL;

-- Rúbrica para RA de Programación I: Resolver problemas computacionales
INSERT INTO rubrica (description, subject_outcome_id, is_activated)
SELECT 'Rúbrica para evaluación de resolución de problemas computacionales', 
       ra.id, 
       true
FROM ra_asignatura ra 
JOIN asignacion_competencia_asignatura aca ON ra.id_competencia = aca.id
JOIN periodo p ON aca.id_periodo = p.id
WHERE ra.description = 'Resolver problemas computacionales mediante algoritmos secuenciales'
AND p.term IN ('2024-1', '2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM rubrica 
  WHERE subject_outcome_id = ra.id
);

UPDATE ra_asignatura
SET rubric_id = (
  SELECT r.id FROM rubrica r 
  WHERE r.subject_outcome_id = ra_asignatura.id 
  AND r.description = 'Rúbrica para evaluación de resolución de problemas computacionales'
)
WHERE description = 'Resolver problemas computacionales mediante algoritmos secuenciales'
AND rubric_id IS NULL;

-- Rúbrica para RA de Programación I: Diseñar clases
INSERT INTO rubrica (description, subject_outcome_id, is_activated)
SELECT 'Rúbrica para evaluación de diseño de clases', 
       ra.id, 
       true
FROM ra_asignatura ra 
JOIN asignacion_competencia_asignatura aca ON ra.id_competencia = aca.id
JOIN periodo p ON aca.id_periodo = p.id
WHERE ra.description = 'Diseñar clases y objetos siguiendo principios de encapsulamiento'
AND p.term IN ('2024-1', '2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM rubrica 
  WHERE subject_outcome_id = ra.id
);

UPDATE ra_asignatura
SET rubric_id = (
  SELECT r.id FROM rubrica r 
  WHERE r.subject_outcome_id = ra_asignatura.id 
  AND r.description = 'Rúbrica para evaluación de diseño de clases'
)
WHERE description = 'Diseñar clases y objetos siguiendo principios de encapsulamiento'
AND rubric_id IS NULL;

-- Rúbrica para RA de Programación I: Implementar herencia
INSERT INTO rubrica (description, subject_outcome_id, is_activated)
SELECT 'Rúbrica para evaluación de herencia y polimorfismo', 
       ra.id, 
       true
FROM ra_asignatura ra 
JOIN asignacion_competencia_asignatura aca ON ra.id_competencia = aca.id
JOIN periodo p ON aca.id_periodo = p.id
WHERE ra.description = 'Implementar herencia y polimorfismo en soluciones de software'
AND p.term IN ('2024-1', '2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM rubrica 
  WHERE subject_outcome_id = ra.id
);

UPDATE ra_asignatura
SET rubric_id = (
  SELECT r.id FROM rubrica r 
  WHERE r.subject_outcome_id = ra_asignatura.id 
  AND r.description = 'Rúbrica para evaluación de herencia y polimorfismo'
)
WHERE description = 'Implementar herencia y polimorfismo en soluciones de software'
AND rubric_id IS NULL;

-- Rúbrica para RA de Estructuras de Datos: Estructuras lineales
INSERT INTO rubrica (description, subject_outcome_id, is_activated)
SELECT 'Rúbrica para evaluación de estructuras de datos lineales', 
       ra.id, 
       true
FROM ra_asignatura ra 
JOIN asignacion_competencia_asignatura aca ON ra.id_competencia = aca.id
JOIN periodo p ON aca.id_periodo = p.id
WHERE ra.description = 'Implementar estructuras de datos lineales (listas, pilas, colas)'
AND p.term IN ('2024-1', '2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM rubrica 
  WHERE subject_outcome_id = ra.id
);

UPDATE ra_asignatura
SET rubric_id = (
  SELECT r.id FROM rubrica r 
  WHERE r.subject_outcome_id = ra_asignatura.id 
  AND r.description = 'Rúbrica para evaluación de estructuras de datos lineales'
)
WHERE description = 'Implementar estructuras de datos lineales (listas, pilas, colas)'
AND rubric_id IS NULL;

-- Rúbrica para RA de Estructuras de Datos: Estructuras jerárquicas
INSERT INTO rubrica (description, subject_outcome_id, is_activated)
SELECT 'Rúbrica para evaluación de estructuras de datos jerárquicas', 
       ra.id, 
       true
FROM ra_asignatura ra 
JOIN asignacion_competencia_asignatura aca ON ra.id_competencia = aca.id
JOIN periodo p ON aca.id_periodo = p.id
WHERE ra.description = 'Implementar estructuras de datos jerárquicas (árboles, grafos)'
AND p.term IN ('2024-1', '2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM rubrica 
  WHERE subject_outcome_id = ra.id
);

UPDATE ra_asignatura
SET rubric_id = (
  SELECT r.id FROM rubrica r 
  WHERE r.subject_outcome_id = ra_asignatura.id 
  AND r.description = 'Rúbrica para evaluación de estructuras de datos jerárquicas'
)
WHERE description = 'Implementar estructuras de datos jerárquicas (árboles, grafos)'
AND rubric_id IS NULL;

-- Rúbrica para RA de Estructuras de Datos: Evaluar complejidad
INSERT INTO rubrica (description, subject_outcome_id, is_activated)
SELECT 'Rúbrica para evaluación de complejidad temporal', 
       ra.id, 
       true
FROM ra_asignatura ra 
JOIN asignacion_competencia_asignatura aca ON ra.id_competencia = aca.id
JOIN periodo p ON aca.id_periodo = p.id
WHERE ra.description = 'Evaluar la complejidad temporal de estructuras de datos'
AND p.term IN ('2024-1', '2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM rubrica 
  WHERE subject_outcome_id = ra.id
);

UPDATE ra_asignatura
SET rubric_id = (
  SELECT r.id FROM rubrica r 
  WHERE r.subject_outcome_id = ra_asignatura.id 
  AND r.description = 'Rúbrica para evaluación de complejidad temporal'
)
WHERE description = 'Evaluar la complejidad temporal de estructuras de datos'
AND rubric_id IS NULL;

-- Rúbrica para RA de Estructuras de Datos: Comparar rendimiento
INSERT INTO rubrica (description, subject_outcome_id, is_activated)
SELECT 'Rúbrica para evaluación de comparación de rendimiento', 
       ra.id, 
       true
FROM ra_asignatura ra 
JOIN asignacion_competencia_asignatura aca ON ra.id_competencia = aca.id
JOIN periodo p ON aca.id_periodo = p.id
WHERE ra.description = 'Comparar el rendimiento de diferentes estructuras de datos'
AND p.term IN ('2024-1', '2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM rubrica 
  WHERE subject_outcome_id = ra.id
);

UPDATE ra_asignatura
SET rubric_id = (
  SELECT r.id FROM rubrica r 
  WHERE r.subject_outcome_id = ra_asignatura.id 
  AND r.description = 'Rúbrica para evaluación de comparación de rendimiento'
)
WHERE description = 'Comparar el rendimiento de diferentes estructuras de datos'
AND rubric_id IS NULL;

-- Rúbrica para RA de Algoritmos: Análisis de complejidad
INSERT INTO rubrica (description, subject_outcome_id, is_activated)
SELECT 'Rúbrica para evaluación de análisis de complejidad algorítmica', 
       ra.id, 
       true
FROM ra_asignatura ra 
JOIN asignacion_competencia_asignatura aca ON ra.id_competencia = aca.id
JOIN periodo p ON aca.id_periodo = p.id
WHERE ra.description = 'Analizar la complejidad temporal y espacial de algoritmos'
AND p.term IN ('2024-1', '2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM rubrica 
  WHERE subject_outcome_id = ra.id
);

UPDATE ra_asignatura
SET rubric_id = (
  SELECT r.id FROM rubrica r 
  WHERE r.subject_outcome_id = ra_asignatura.id 
  AND r.description = 'Rúbrica para evaluación de análisis de complejidad algorítmica'
)
WHERE description = 'Analizar la complejidad temporal y espacial de algoritmos'
AND rubric_id IS NULL;

-- Rúbrica para RA de Algoritmos: Técnicas de análisis asintótico
INSERT INTO rubrica (description, subject_outcome_id, is_activated)
SELECT 'Rúbrica para evaluación de técnicas de análisis asintótico', 
       ra.id, 
       true
FROM ra_asignatura ra 
JOIN asignacion_competencia_asignatura aca ON ra.id_competencia = aca.id
JOIN periodo p ON aca.id_periodo = p.id
WHERE ra.description = 'Aplicar técnicas de análisis asintótico en algoritmos'
AND p.term IN ('2024-1', '2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM rubrica 
  WHERE subject_outcome_id = ra.id
);

UPDATE ra_asignatura
SET rubric_id = (
  SELECT r.id FROM rubrica r 
  WHERE r.subject_outcome_id = ra_asignatura.id 
  AND r.description = 'Rúbrica para evaluación de técnicas de análisis asintótico'
)
WHERE description = 'Aplicar técnicas de análisis asintótico en algoritmos'
AND rubric_id IS NULL;

-- Rúbrica para RA de Algoritmos: Diseño de algoritmos
INSERT INTO rubrica (description, subject_outcome_id, is_activated)
SELECT 'Rúbrica para evaluación de diseño de algoritmos eficientes', 
       ra.id, 
       true
FROM ra_asignatura ra 
JOIN asignacion_competencia_asignatura aca ON ra.id_competencia = aca.id
JOIN periodo p ON aca.id_periodo = p.id
WHERE ra.description = 'Diseñar algoritmos eficientes para problemas complejos'
AND p.term IN ('2024-1', '2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM rubrica 
  WHERE subject_outcome_id = ra.id
);

UPDATE ra_asignatura
SET rubric_id = (
  SELECT r.id FROM rubrica r 
  WHERE r.subject_outcome_id = ra_asignatura.id 
  AND r.description = 'Rúbrica para evaluación de diseño de algoritmos eficientes'
)
WHERE description = 'Diseñar algoritmos eficientes para problemas complejos'
AND rubric_id IS NULL;

-- Rúbrica para RA de Algoritmos: Optimización de algoritmos
INSERT INTO rubrica (description, subject_outcome_id, is_activated)
SELECT 'Rúbrica para evaluación de optimización de algoritmos', 
       ra.id, 
       true
FROM ra_asignatura ra 
JOIN asignacion_competencia_asignatura aca ON ra.id_competencia = aca.id
JOIN periodo p ON aca.id_periodo = p.id
WHERE ra.description = 'Optimizar algoritmos existentes mediante técnicas avanzadas'
AND p.term IN ('2024-1', '2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM rubrica 
  WHERE subject_outcome_id = ra.id
);

UPDATE ra_asignatura
SET rubric_id = (
  SELECT r.id FROM rubrica r 
  WHERE r.subject_outcome_id = ra_asignatura.id 
  AND r.description = 'Rúbrica para evaluación de optimización de algoritmos'
)
WHERE description = 'Optimizar algoritmos existentes mediante técnicas avanzadas'
AND rubric_id IS NULL;

-- 11. Insertar criterios para cada rúbrica
-- IMPORTANTE: Cada rúbrica debe tener criterios que sumen exactamente 100%

-- Criterios para rúbrica de implementación de algoritmos básicos
INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Corrección funcional', 50, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de implementación de algoritmos'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Corrección funcional'
);

INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Legibilidad del código', 30, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de implementación de algoritmos'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Legibilidad del código'
);

INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Eficiencia algorítmica', 20, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de implementación de algoritmos'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Eficiencia algorítmica'
);

-- Criterios para rúbrica de resolución de problemas computacionales (suma = 100%)
INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Análisis del problema', 35, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de resolución de problemas computacionales'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Análisis del problema'
);

INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Diseño de la solución', 40, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de resolución de problemas computacionales'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Diseño de la solución'
);

INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Implementación secuencial', 25, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de resolución de problemas computacionales'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Implementación secuencial'
);

-- Criterios para rúbrica de diseño de clases (suma = 100%)
INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Aplicación de encapsulamiento', 40, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de diseño de clases'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Aplicación de encapsulamiento'
);

INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Diseño de interfaces', 35, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de diseño de clases'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Diseño de interfaces'
);

INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Cohesión y acoplamiento', 25, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de diseño de clases'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Cohesión y acoplamiento'
);

-- Criterios para rúbrica de herencia y polimorfismo (suma = 100%)
INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Implementación de herencia', 45, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de herencia y polimorfismo'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Implementación de herencia'
);

INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Aplicación de polimorfismo', 35, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de herencia y polimorfismo'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Aplicación de polimorfismo'
);

INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Principios SOLID básicos', 20, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de herencia y polimorfismo'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Principios SOLID básicos'
);

-- Criterios para rúbrica de estructuras de datos lineales (suma = 100%)
INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Implementación correcta', 40, r.id
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

INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Operaciones básicas (insertar, eliminar, buscar)', 30, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de estructuras de datos lineales'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Operaciones básicas (insertar, eliminar, buscar)'
);

-- Criterios para rúbrica de estructuras de datos jerárquicas (suma = 100%)
INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Implementación de operaciones', 35, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de estructuras de datos jerárquicas'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Implementación de operaciones'
);

INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Eficiencia de la implementación', 30, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de estructuras de datos jerárquicas'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Eficiencia de la implementación'
);

INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Manejo de recorridos (pre, in, post-order)', 35, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de estructuras de datos jerárquicas'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Manejo de recorridos (pre, in, post-order)'
);

-- Criterios para rúbrica de evaluación de complejidad temporal (suma = 100%)
INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Cálculo de complejidad O(n)', 45, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de complejidad temporal'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Cálculo de complejidad O(n)'
);

INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Justificación matemática', 35, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de complejidad temporal'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Justificación matemática'
);

INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Comparación entre estructuras', 20, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de complejidad temporal'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Comparación entre estructuras'
);

-- Criterios para rúbrica de comparación de rendimiento (suma = 100%)
INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Metodología de comparación', 40, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de comparación de rendimiento'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Metodología de comparación'
);

INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Análisis de resultados', 35, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de comparación de rendimiento'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Análisis de resultados'
);

INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Selección apropiada según contexto', 25, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de comparación de rendimiento'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Selección apropiada según contexto'
);

-- Criterios para rúbrica de análisis de complejidad algorítmica (suma = 100%)
INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Análisis matemático', 40, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de análisis de complejidad algorítmica'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Análisis matemático'
);

INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Identificación de cuellos de botella', 35, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de análisis de complejidad algorítmica'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Identificación de cuellos de botella'
);

INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Comparación de complexidades', 25, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de análisis de complejidad algorítmica'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Comparación de complexidades'
);

-- Criterios para rúbrica de técnicas de análisis asintótico (suma = 100%)
INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Aplicación de notación Big O', 45, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de técnicas de análisis asintótico'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Aplicación de notación Big O'
);

INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Análisis de casos (mejor, promedio, peor)', 30, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de técnicas de análisis asintótico'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Análisis de casos (mejor, promedio, peor)'
);

INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Demostración formal de límites', 25, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de técnicas de análisis asintótico'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Demostración formal de límites'
);

-- Criterios para rúbrica de diseño de algoritmos eficientes (suma = 100%)
INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Elección de estrategia algorítmica', 40, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de diseño de algoritmos eficientes'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Elección de estrategia algorítmica'
);

INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Optimización de recursos', 35, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de diseño de algoritmos eficientes'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Optimización de recursos'
);

INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Escalabilidad de la solución', 25, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de diseño de algoritmos eficientes'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Escalabilidad de la solución'
);

-- Criterios para rúbrica de optimización de algoritmos (suma = 100%)
INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Identificación de oportunidades de mejora', 35, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de optimización de algoritmos'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Identificación de oportunidades de mejora'
);

INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Aplicación de técnicas avanzadas', 40, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de optimización de algoritmos'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Aplicación de técnicas avanzadas'
);

INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Validación de mejoras obtenidas', 25, r.id
FROM rubrica r 
WHERE r.description = 'Rúbrica para evaluación de optimización de algoritmos'
AND NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Validación de mejoras obtenidas'
);
-- 12. Insertar niveles para cada criterio
-- IMPORTANTE: Cada criterio debe tener máximo 5 niveles y deben sumar 100%

-- Niveles estándar para todos los criterios (4 niveles que suman 100%)
-- Nivel Excelente (25%)
INSERT INTO nivel (category, description, percentage, id_criterio)
SELECT 'Excelente', 'Dominio completo y aplicación innovadora del criterio', 25, c.id
FROM criterio c
WHERE NOT EXISTS (
  SELECT 1 FROM nivel 
  WHERE id_criterio = c.id AND category = 'Excelente'
);

-- Nivel Competente (25%)
INSERT INTO nivel (category, description, percentage, id_criterio)
SELECT 'Competente', 'Comprensión sólida y aplicación correcta del criterio', 25, c.id
FROM criterio c
WHERE NOT EXISTS (
  SELECT 1 FROM nivel 
  WHERE id_criterio = c.id AND category = 'Competente'
);

-- Nivel En desarrollo (25%)
INSERT INTO nivel (category, description, percentage, id_criterio)
SELECT 'En desarrollo', 'Comprensión básica con aplicación parcial del criterio', 25, c.id
FROM criterio c
WHERE NOT EXISTS (
  SELECT 1 FROM nivel 
  WHERE id_criterio = c.id AND category = 'En desarrollo'
);

-- Nivel Inicial (25%)
INSERT INTO nivel (category, description, percentage, id_criterio)
SELECT 'Inicial', 'Comprensión mínima del criterio, requiere mucha mejora', 25, c.id
FROM criterio c
WHERE NOT EXISTS (
  SELECT 1 FROM nivel 
  WHERE id_criterio = c.id AND category = 'Inicial'
);

-- Fin del script de carga por defecto
