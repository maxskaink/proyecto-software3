-- Insertar periodos académicos con validación
INSERT INTO periodo (term)
SELECT '2024-1' WHERE NOT EXISTS (SELECT 1 FROM periodo WHERE term = '2024-1');

INSERT INTO periodo (term)
SELECT '2024-2' WHERE NOT EXISTS (SELECT 1 FROM periodo WHERE term = '2024-2');

INSERT INTO periodo (term)
SELECT '2025-1' WHERE NOT EXISTS (SELECT 1 FROM periodo WHERE term = '2025-1');

-- Insertar configuración solo si no existe
INSERT INTO configuracion (periodo_actual_id)
SELECT 1 WHERE NOT EXISTS (SELECT 1 FROM configuracion);

-- Insertar asignaturas con validación
INSERT INTO asignatura (name, description, is_activated)
SELECT 'Programación I', 'Fundamentos de programación en Java', true
WHERE NOT EXISTS (SELECT 1 FROM asignatura WHERE name = 'Programación I');

INSERT INTO asignatura (name, description, is_activated)
SELECT 'Estructuras de Datos', 'Uso de estructuras básicas como listas, pilas, colas y árboles', true
WHERE NOT EXISTS (SELECT 1 FROM asignatura WHERE name = 'Estructuras de Datos');
