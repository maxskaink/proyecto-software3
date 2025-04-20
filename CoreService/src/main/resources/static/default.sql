-- Insertar periodos académicos
INSERT INTO periodo (periodo) VALUES
                                  ('2024-1'),
                                  ('2024-2'),
                                  ('2025-1');
INSERT INTO configuracion (periodo_actual_id) VALUES (1);

-- Insertar asignaturas
INSERT INTO asignatura (nombre, descripcion, activado) VALUES
                                                           ('Programación I', 'Fundamentos de programación en Java', true),
                                                           ('Estructuras de Datos', 'Uso de estructuras básicas como listas, pilas, colas y árboles', true);


INSERT INTO asignacion_docente (id_periodo, id_asignatura, id_docente) VALUES
                                                                           (1, 1, 1),
                                                                           (1, 2, 2);
