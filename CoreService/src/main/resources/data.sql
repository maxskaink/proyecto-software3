-- Script de carga de datos por defecto con validación para CoreService
-- Este script crea competencias de programa, resultados de programa, competencias de asignatura,
-- asignaciones de competencias, resultados de asignatura, rúbricas, criterios y niveles para tres periodos
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
INSERT INTO competencia_asignatura (description, id_competencia_peograma, is_activated)
SELECT 'CA: ' || cp.description, cp.id, true
FROM competencia_programa cp
WHERE NOT EXISTS (
  SELECT 1 FROM competencia_asignatura 
  WHERE description = 'CA: ' || cp.description
);

-- 8. Asignar competencias de asignatura a asignaturas en periodos específicos
INSERT INTO asignacion_competencia_asignatura (id_competencia, id_asignatura, id_periodo, is_activated)
SELECT ca.id, a.id, p.id, true
FROM competencia_asignatura ca, asignatura a, periodo p
WHERE p.term IN ('2024-1', '2024-2', '2025-1')
AND NOT EXISTS (
  SELECT 1 FROM asignacion_competencia_asignatura 
  WHERE id_competencia = ca.id AND id_asignatura = a.id AND id_periodo = p.id
);

-- 9. Insertar resultados de aprendizaje de asignatura para cada asignación de competencia
-- Crear primer RA para cada asignación
INSERT INTO ra_asignatura (description, id_competencia, is_activated)
SELECT 'RA de asignatura 1', aca.id, true
FROM asignacion_competencia_asignatura aca
WHERE NOT EXISTS (
  SELECT 1 FROM ra_asignatura 
  WHERE id_competencia = aca.id AND description = 'RA de asignatura 1'
);

-- Crear segundo RA para cada asignación
INSERT INTO ra_asignatura (description, id_competencia, is_activated)
SELECT 'RA de asignatura 2', aca.id, true
FROM asignacion_competencia_asignatura aca
WHERE NOT EXISTS (
  SELECT 1 FROM ra_asignatura 
  WHERE id_competencia = aca.id AND description = 'RA de asignatura 2'
);

-- 10. Insertar rúbricas para cada resultado de aprendizaje de asignatura
INSERT INTO rubrica (description, subject_outcome_id, is_activated)
SELECT 'Rúbrica para evaluación', ra.id, true
FROM ra_asignatura ra
WHERE NOT EXISTS (
  SELECT 1 FROM rubrica 
  WHERE subject_outcome_id = ra.id
);

-- 11. Insertar criterios para cada rúbrica
-- Criterio 1 para cada rúbrica
INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Criterio 1', 0.5, r.id
FROM rubrica r
WHERE NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Criterio 1'
);

-- Criterio 2 para cada rúbrica
INSERT INTO criterio (name, weight, rubric_id)
SELECT 'Criterio 2', 0.5, r.id
FROM rubrica r
WHERE NOT EXISTS (
  SELECT 1 FROM criterio 
  WHERE rubric_id = r.id AND name = 'Criterio 2'
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
