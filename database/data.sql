-- Datos actuales de cafeland_club.
-- Ejecutar después de schema.sql.

BEGIN;

-- 1. Tablas independientes y catálogos
INSERT INTO public.rangos (id, nombre) VALUES
    (1, 'LIDER'),
    (2, 'VETERANO'),
    (4, 'MODERADOR');

INSERT INTO public.ligas (id, minimo_puntos_torneo, numero) VALUES
    (1, 5000, 4),
    (2, 4500, 5),
    (3, 4000, 6);

-- 2. Depende de rangos
INSERT INTO public.miembros (id, estado, fecha_ingreso, nombre, rango_id) VALUES
    (1, 'ACTIVO', DATE '2026-07-20', 'CAFE BUBU', 4),
    (2, 'ACTIVO', DATE '2026-07-20', 'CAFE DUDU', 1),
    (3, 'ACTIVO', DATE '2026-08-02', 'CAFE SIN AZUCAR', 4),
    (4, 'ACTIVO', DATE '2026-08-17', 'AYUDANTE DE DUDU 2', 2),
    (5, 'ACTIVO', DATE '2026-08-21', 'fanitania', 2);

-- 3. Depende de ligas
INSERT INTO public.semanas (id, fecha_fin, fecha_inicio, liga_id, numero_semana) VALUES
    (1, DATE '2026-08-23', DATE '2026-08-17', 1, 4),
    (2, DATE '2026-08-16', DATE '2026-08-10', 1, 3);

-- 4. Depende de miembros y semanas
INSERT INTO public.resultados_semanales
    (id, puntos_desafio, puntos_torneo, miembro_id, semana_id)
VALUES
    (1, 2000, 5000, 1, 1),
    (2, 2500, 5600, 2, 1);

-- Sincroniza las identidades con los datos restaurados.
SELECT pg_catalog.setval(
    pg_get_serial_sequence('public.rangos', 'id'),
    COALESCE((SELECT MAX(id) FROM public.rangos), 1),
    EXISTS (SELECT 1 FROM public.rangos)
);
SELECT pg_catalog.setval(
    pg_get_serial_sequence('public.ligas', 'id'),
    COALESCE((SELECT MAX(id) FROM public.ligas), 1),
    EXISTS (SELECT 1 FROM public.ligas)
);
SELECT pg_catalog.setval(
    pg_get_serial_sequence('public.miembros', 'id'),
    COALESCE((SELECT MAX(id) FROM public.miembros), 1),
    EXISTS (SELECT 1 FROM public.miembros)
);
SELECT pg_catalog.setval(
    pg_get_serial_sequence('public.semanas', 'id'),
    COALESCE((SELECT MAX(id) FROM public.semanas), 1),
    EXISTS (SELECT 1 FROM public.semanas)
);
SELECT pg_catalog.setval(
    pg_get_serial_sequence('public.resultados_semanales', 'id'),
    COALESCE((SELECT MAX(id) FROM public.resultados_semanales), 1),
    EXISTS (SELECT 1 FROM public.resultados_semanales)
);

COMMIT;
