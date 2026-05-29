# INSERT INTO pour la base de données e-sport

-- =========================
-- INSERTION DES EDITEURS
-- =========================

INSERT INTO Editeur (nom_editeur) VALUES
('Riot Games'),
('Valve'),
('EA Sports'),
('Epic Games');


-- =========================
-- INSERTION DES JEUX
-- =========================

INSERT INTO Jeu (nom_jeu, genre, annee_de_sortie, id_editeur) VALUES
('League of Legends', 'MOBA', 2009, 1),
('Valorant', 'FPS', 2020, 1),
('Counter-Strike 2', 'FPS', 2023, 2),
('EA Sports FC 25', 'Sport', 2024, 3);


-- =========================
-- INSERTION DES EQUIPES
-- =========================

INSERT INTO Equipe (nom_equipe, logo, date_creation, pays_origine) VALUES
('Karmine Corp', 'logos/kcorp.png', '2020-03-31', 'France'),
('Team Vitality', 'logos/vitality.png', '2013-08-05', 'France'),
('Fnatic', 'logos/fnatic.png', '2004-07-23', 'Royaume-Uni'),
('G2 Esports', 'logos/g2.png', '2015-02-24', 'Allemagne'),
('T1', 'logos/t1.png', '2003-12-13', 'Corée du Sud');


-- =========================
-- INSERTION DES JOUEURS
-- =========================

INSERT INTO Joueurs (pseudo, nom_joueur, prenom_joueur, date_naissance, nationalite, niveau_elo) VALUES
('Faker', 'Lee', 'Sang-hyeok', '1996-05-07', 'Corée du Sud', 2900),
('Caps', 'Winther', 'Rasmus', '1999-11-17', 'Danemark', 2700),
('Boaster', 'Howlett', 'Jake', '1995-05-25', 'Royaume-Uni', 2500),
('ZywOo', 'Herbaut', 'Mathieu', '2000-11-09', 'France', 2950),
('Vatira', 'Benkirat', 'Axel', '2005-07-24', 'France', 2400),
('TenZ', 'Ngo', 'Tyson', '2001-05-05', 'Canada', 2650),
('s1mple', 'Kostyliev', 'Oleksandr', '1997-10-02', 'Ukraine', 3000),
('Rekkles', 'Larsson', 'Martin', '1996-09-20', 'Suède', 2600),
('Perkz', 'Perkovic', 'Luka', '1998-09-30', 'Croatie', 2550),
('Niko', 'Kovacs', 'Nikola', '1997-02-16', 'Bosnie', 2850),
('Yike', 'Sari', 'Martin', '2000-10-15', 'Suède', 2450),
('Aspas', 'Santos', 'Erick', '2003-06-03', 'Brésil', 2750),
('Derke', 'Sirmitev', 'Nikita', '2003-02-06', 'Finlande', 2680),
('Keria', 'Ryu', 'Min-seok', '2002-10-14', 'Corée du Sud', 2780),
('Oner', 'Mun', 'Hyeon-jun', '2002-12-24', 'Corée du Sud', 2760),
('Zeus', 'Choi', 'Woo-je', '2004-01-31', 'Corée du Sud', 2790),
('Gumayusi', 'Lee', 'Min-hyeong', '2002-02-06', 'Corée du Sud', 2770),
('Sh1n', 'Kim', 'Seungwon', '2004-03-15', 'Corée du Sud', 2400),
('Exotiik', 'Marty', 'Brice', '2001-06-14', 'France', 2380),
('Alpha54', 'Arnould', 'Yanis', '2003-08-27', 'France', 2420);


-- =========================
-- INSERTION DES TOURNOIS
-- =========================

INSERT INTO Tournoi (nom_tournoi, date_debut_tournoi, date_fin_tournoi, type, dotation, statut, id_jeu) VALUES
('Worlds 2025', '2025-10-01', '2025-11-02', 'LAN', 2500000.00, 'À venir', 1),
('Valorant Champions 2025', '2025-08-10', '2025-08-30', 'LAN', 1500000.00, 'À venir', 2),
('Major CS2 Paris', '2025-09-12', '2025-09-25', 'LAN', 1800000.00, 'À venir', 3);


-- =========================
-- INSERTION DES PHASES
-- =========================

INSERT INTO Phase (nom_phase, id_tournoi) VALUES
('Phase de groupes', 1),
('Demi-finales', 1),
('Finale', 1),

('Phase de groupes', 2),
('Demi-finales', 2),
('Finale', 2),

('Phase de groupes', 3),
('Demi-finales', 3),
('Finale', 3);


-- =========================
-- INSERTION DES MATCHS
-- =========================

INSERT INTO `Match`
(date_match, score_equipe1, score_equipe2, resultat, nombre_maps, id_phase, id_equipe_1, id_equipe_2)
VALUES
('2025-10-02 18:00:00', 2, 1, 'Equipe 1', 3, 1, 1, 2),
('2025-10-03 20:00:00', 2, 0, 'Equipe 1', 2, 1, 3, 4),
('2025-10-10 19:00:00', 3, 2, 'Equipe 1', 5, 2, 1, 4),
('2025-11-02 21:00:00', 3, 1, 'Equipe 1', 4, 3, 1, 5),

('2025-08-11 17:00:00', 2, 1, 'Equipe 1', 3, 4, 2, 3),
('2025-08-12 18:30:00', 2, 0, 'Equipe 1', 2, 4, 1, 4),
('2025-08-20 20:00:00', 3, 2, 'Equipe 2', 5, 5, 2, 4),
('2025-08-30 22:00:00', 3, 0, 'Equipe 1', 3, 6, 4, 1),

('2025-09-13 16:00:00', 2, 1, 'Equipe 2', 3, 7, 2, 5),
('2025-09-14 19:00:00', 2, 0, 'Equipe 1', 2, 7, 3, 1),
('2025-09-20 20:00:00', 3, 1, 'Equipe 1', 4, 8, 2, 3),
('2025-09-25 21:00:00', 3, 2, 'Equipe 2', 5, 9, 2, 1),

('2025-09-26 18:00:00', 1, 0, 'Equipe 1', 1, 7, 4, 5),
('2025-09-27 19:00:00', 0, 1, 'Equipe 2', 1, 8, 1, 5),
('2025-09-28 20:00:00', 2, 1, 'Equipe 1', 3, 9, 3, 4);


-- =========================
-- INSERTION DES ROSTERS
-- =========================

INSERT INTO Roster (id_jeu, id_joueur, id_equipe, date_debut, date_fin) VALUES
(1, 1, 5, '2023-01-01', NULL),
(1, 14, 5, '2023-01-01', NULL),
(1, 15, 5, '2023-01-01', NULL),
(1, 16, 5, '2023-01-01', NULL),
(1, 17, 5, '2023-01-01', NULL),

(1, 2, 4, '2024-01-01', NULL),
(1, 8, 3, '2023-01-01', NULL),
(1, 9, 4, '2023-01-01', NULL),
(1, 11, 4, '2024-01-01', NULL),

(2, 3, 2, '2024-01-01', NULL),
(2, 6, 1, '2024-01-01', NULL),
(2, 12, 3, '2024-01-01', NULL),
(2, 13, 3, '2024-01-01', NULL),

(3, 4, 2, '2022-01-01', NULL),
(3, 7, 4, '2021-01-01', NULL),
(3, 10, 4, '2021-01-01', NULL),
(3, 18, 2, '2024-01-01', NULL),

(4, 5, 1, '2024-01-01', NULL),
(4, 19, 1, '2024-01-01', NULL),
(4, 20, 2, '2024-01-01', NULL);


-- =========================
-- INSERTION DES STATISTIQUES
-- =========================

INSERT INTO Statistiques
(id_joueur, id_match, nb_kills, nb_deaths, nb_assists, score)
VALUES
(1,1,10,2,8,95),
(2,1,8,4,6,82),
(3,1,12,5,7,90),
(4,1,15,3,5,98),

(5,2,9,2,4,80),
(6,2,11,6,8,85),
(7,2,14,5,3,91),
(8,2,7,4,10,79),

(9,3,13,6,5,88),
(10,3,16,7,4,96),
(11,3,10,3,11,89),
(12,3,8,5,7,78),

(13,4,17,6,9,99),
(14,4,11,4,12,92),
(15,4,9,5,6,81),
(16,4,14,7,5,90),

(17,5,15,4,8,94),
(18,5,8,6,7,76),
(19,5,10,5,9,84),
(20,5,12,3,4,87),

(1,6,13,5,10,91),
(2,6,9,4,6,80),
(3,6,11,7,5,82),
(4,6,18,3,7,100),

(5,7,14,6,8,89),
(6,7,10,5,9,83),
(7,7,16,4,5,95),
(8,7,7,8,6,70),

(9,8,12,4,11,90),
(10,8,17,5,4,98),
(11,8,8,3,10,79),
(12,8,9,7,6,74),

(13,9,15,6,8,93),
(14,9,10,4,9,85),
(15,9,13,5,7,88),
(16,9,11,6,12,86),

(17,10,14,5,6,90),
(18,10,9,7,8,75),
(19,10,12,4,5,84),
(20,10,16,3,7,97),

(1,11,13,6,8,89),
(2,11,8,5,11,78),
(3,11,12,4,7,86),
(4,11,17,5,4,99),

(5,12,10,7,9,80),
(6,12,15,3,6,94),
(7,12,18,4,5,100),
(8,12,9,6,8,77),

(9,13,11,5,7,84),
(10,13,14,4,5,92),
(11,13,8,6,12,76),
(12,13,13,7,6,85),

(13,14,16,5,8,96),
(14,14,10,4,9,82),
(15,14,12,6,7,87),
(16,14,15,5,6,93),

(17,15,13,3,10,91),
(18,15,9,5,8,79),
(19,15,11,4,7,85),
(20,15,14,6,5,90);
```
