-- Insertar 4 recetas en la tabla recipes
INSERT INTO recipes (slug, name, short_description, full_description, image_filename, category) VALUES
('huevos-revueltos-granja', 'Huevos Revueltos Clásicos de Granja', 'Un desayuno rápido, cremoso y nutritivo, perfecto para empezar el día.', 'Nuestros huevos revueltos son una prueba de la frescura de nuestros productos. La clave es usar fuego bajo y batir suavemente. Sirve con pan tostado o aguacate.', 'revueltos.webp', 'breakfast'),
('omelette-espinacas-queso', 'Omelette de Queso y Espinacas', 'Omelette ligero, esponjoso y relleno de vegetales orgánicos.', 'Este omelette es una opción elegante y saludable. Usamos queso de cabra para un sabor más intenso. Perfecto para un almuerzo o cena rápida.', 'omelette.webp', 'lunch'),
('pastel-canela-esponjoso', 'Pastel Rústico de Canela y Pasas', 'Postre tradicional con el toque dulce de la canela y una textura suave.', 'Este pastel es ideal para las tardes frías. La yema de nuestros huevos orgánicos aporta un color dorado y una humedad inigualable. No olvides añadir el toque final de azúcar glass.', 'pastel-canela.webp', 'dessert'),
('mayonesa-casera-limon', 'Mayonesa Casera Orgánica Rápida', 'Aderezo esencial, cremoso y vibrante, hecho en menos de 5 minutos solo con yemas de huevo.', 'Hacer mayonesa en casa garantiza un producto sin conservadores. El truco es usar huevos a temperatura ambiente y añadir el aceite muy lentamente para emulsificar.', 'mayonesa.webp', 'dip');

-- Ingredientes para ID 1: Huevos Revueltos
INSERT INTO ingredients (recipe_id, ingredient_text) VALUES
(1, '2 Huevos orgánicos grandes'),
(1, '1 cucharada de mantequilla sin sal'),
(1, 'Sal marina y pimienta fresca al gusto');

-- Ingredientes para ID 2: Omelette de Espinacas
INSERT INTO ingredients (recipe_id, ingredient_text) VALUES
(2, '2 Huevos orgánicos grandes'),
(2, '1 puñado de espinacas frescas'),
(2, '30g de queso de cabra desmoronado'),
(2, '1 cucharada de aceite de oliva');

-- Ingredientes para ID 3: Pastel de Canela
INSERT INTO ingredients (recipe_id, ingredient_text) VALUES
(3, '3 Huevos orgánicos'),
(3, '1 taza de harina de trigo'),
(3, '1/2 taza de leche entera'),
(3, '2 cucharaditas de canela en polvo');

-- Ingredientes para ID 4: Mayonesa Casera
INSERT INTO ingredients (recipe_id, ingredient_text) VALUES
(4, '2 Yemas de huevo orgánico (a temperatura ambiente)'),
(4, '1/2 taza de aceite vegetal neutro'),
(4, '1 cucharada de jugo de limón'),
(4, '1/2 cucharadita de mostaza Dijon');

-- Pasos para ID 1: Huevos Revueltos
INSERT INTO steps (recipe_id, `step_order`, instruction) VALUES
(1, 1, 'Rompe los huevos en un tazón, sazona con sal y pimienta, y bátelos suavemente.'),
(1, 2, 'Derrite la mantequilla en una sartén antiadherente a fuego bajo.'),
(1, 3, 'Vierte los huevos y revuelve lentamente hasta que estén cuajados al gusto.'),
(1, 4, 'Retira del fuego y sirve inmediatamente.');

-- Pasos para ID 2: Omelette de Espinacas
INSERT INTO steps (recipe_id, `step_order`, instruction) VALUES
(2, 1, 'Bate los huevos con un chorrito de agua y sazona.'),
(2, 2, 'Calienta el aceite de oliva en la sartén y vierte la mezcla de huevo.'),
(2, 3, 'Antes de que el huevo cuaje por completo, agrega las espinacas y el queso de cabra.'),
(2, 4, 'Dobla el omelette por la mitad y cocina por 1 minuto más.');

-- Pasos para ID 3: Pastel de Canela
INSERT INTO steps (recipe_id, `step_order`, instruction) VALUES
(3, 1, 'Precalienta el horno a 180°C y engrasa un molde.'),
(3, 2, 'Mezcla los ingredientes secos (harina, canela).'),
(3, 3, 'En otro bol, bate los huevos con azúcar hasta que estén esponjosos y luego añade la leche.'),
(3, 4, 'Combina ambas mezclas y vierte en el molde. Hornea por 35-40 minutos.');

-- Pasos para ID 4: Mayonesa Casera
INSERT INTO steps (recipe_id, `step_order`, instruction) VALUES
(4, 1, 'En una licuadora o procesador, combina las yemas, mostaza y limón.'),
(4, 2, 'Con el motor encendido, vierte el aceite muy lentamente en un hilo fino (¡crucial para emulsificar!).'),
(4, 3, 'Continúa batiendo hasta que la mayonesa espese y adquiera consistencia.'),
(4, 4, 'Sazona con sal y disfruta.');

-- Insertar productos en la tabla products
INSERT INTO products (name, description, base_price, category, main_image, active) VALUES
('Huevos Orgánicos', 'Nuestros huevos provienen de gallinas criadas con mínimo uso de químicos, en un ambiente cuidado y natural.', 24.00, 'huevos', 'img/sixeggs.webp', true),
('Abono Orgánico', 'Abono orgánico elaborado a partir del estiércol compostado de gallinas.', 80.00, 'abono', 'img/abono.webp', true),
('Lote de Gallinas', 'Gallinas de postura de alta calidad', 280.00, 'aves', 'img/chicken2.webp', true);

-- Ingredientes para ID 1: Huevos Revueltos
INSERT INTO product_variant (product_id, name, price, image, stock) VALUES
(1, 'Paquete de 6 Huevos', 24.00, '../img/sixeggs.webp', 100),
(1, 'Paquete de 12 Huevos', 40.00, '../img/eggs12.webp', 80),
(1, 'Paquete de 30 Huevos', 95.00, '../img/eggs30.webp', 50),
(1, 'Caja de 320 Huevos', 980.00, '../img/eggs12.webp', 20),
(2, 'Bolsa de 1 Kg', 80.00, '../img/abono.webp', 200),
(2, 'Bolsaza de 6 Kg', 280.00, '../img/abono.webp', 100),
(2, 'Costal de 10 Kg', 320.00, '../img/abono.webp', 80),
(2, 'Costal de 15 Kg', 400.00, '../img/abono.webp', 60),
(2, 'Costal de 25 Kg', 620.00, '../img/abono.webp', 40),
(3, 'Lote de 25 Aves', 280.00, '../img/chicken2.webp', 15),
(3, 'Lote de 50 Aves', 500.00, '../img/chicken2.webp', 10),
(3, 'Lote de 100 Aves', 950.00, '../img/chicken2.webp', 5);

-- Suscripciones de prueba para la tabla newsletter
INSERT INTO newsletter (email, active) VALUES
('maria.gomez@example.com', TRUE),
('juan.perez@example.com', TRUE),
('laura.lopez@example.com', TRUE),
('carlos.ramirez@example.com', TRUE),
('ana.martinez@example.com', TRUE),
('pedro.castillo@example.com', TRUE);