-- R1 : Liste des joueurs triés par pseudo
SELECT pseudo, nom_joueur, prenom_joueur, nationalite
FROM Joueurs
ORDER BY pseudo ASC;

-- R2 : Tournois avec dotation > 10000€
SELECT t.nom_tournoi, j.nom_jeu, t.dotation
FROM Tournoi t
JOIN Jeu j ON t.id_jeu = j.id_jeu
WHERE t.dotation > 10000;

-- R3 : Matchs d’un tournoi donné
SELECT m.date_match, e1.nom_equipe AS equipe1, e2.nom_equipe AS equipe2,
       m.score_equipe1, m.score_equipe2
FROM `Match` m
JOIN Phase p ON m.id_phase = p.id_phase
JOIN Tournoi t ON p.id_tournoi = t.id_tournoi
JOIN Equipe e1 ON m.id_equipe_1 = e1.id_equipe
JOIN Equipe e2 ON m.id_equipe_2 = e2.id_equipe
WHERE t.id_tournoi = 1;

-- R4 : joueurs d’une équipe pour un jeu donné
SELECT e.nom_equipe, j.pseudo, j.nom_joueur, j.prenom_joueur
FROM Roster r
JOIN Joueurs j ON r.id_joueur = j.id_joueur
JOIN Equipe e ON m.id_equipe_1 = e.id_equipe OR m.id_equipe_2 = e.id_equipe
AND m.resultat = 'Equipe 1'

-- R5 : vainqueur de chaque tournoi terminé
-- R5 : vainqueur des tournois terminés
SELECT DISTINCT t.nom_tournoi, t.dotation,
       CASE 
           WHEN m.resultat = 'Equipe 1' THEN e1.nom_equipe
           WHEN m.resultat = 'Equipe 2' THEN e2.nom_equipe
       END AS vainqueur
FROM Tournoi t
JOIN Phase p ON p.id_tournoi = t.id_tournoi
JOIN `Match` m ON m.id_phase = p.id_phase
JOIN Equipe e1 ON m.id_equipe_1 = e1.id_equipe
JOIN Equipe e2 ON m.id_equipe_2 = e2.id_equipe
WHERE t.statut = 'Terminé';

-- R6 : stats agrégées par équipe
SELECT m.id_match, e.nom_equipe,
       SUM(s.nb_kills) AS kills,
       SUM(s.nb_deaths) AS deaths
FROM Statistiques s
JOIN `Match` m ON s.id_match = m.id_match
JOIN Equipe e ON (
    e.id_equipe = m.id_equipe_1 OR e.id_equipe = m.id_equipe_2
)
GROUP BY m.id_match, e.id_equipe;

-- R7 : nombre de tournois par jeu
SELECT j.nom_jeu, COUNT(t.id_tournoi) AS nb_tournois
FROM Jeu j
LEFT JOIN Tournoi t ON j.id_jeu = t.id_jeu
GROUP BY j.id_jeu
ORDER BY nb_tournois DESC;

-- R8 : équipes ayant participé à au moins 2 tournois
SELECT e.nom_equipe
FROM Equipe e
JOIN `Match` m 
  ON e.id_equipe = m.id_equipe_1 
  OR e.id_equipe = m.id_equipe_2
JOIN Phase p ON m.id_phase = p.id_phase
GROUP BY e.id_equipe
HAVING COUNT(DISTINCT p.id_tournoi) >= 2;

-- R9 : meilleur joueur ratio kills/deaths
SELECT j.pseudo,
       SUM(s.nb_kills)/NULLIF(SUM(s.nb_deaths),0) AS ratio
FROM Joueurs j
JOIN Statistiques s ON j.id_joueur = s.id_joueur
GROUP BY j.id_joueur
HAVING COUNT(s.id_match) >= 5
ORDER BY ratio DESC
LIMIT 1;

-- R10 : moyenne des performances par équipe et tournoi
SELECT t.nom_tournoi, e.nom_equipe,
       AVG(s.score) AS moyenne_score
FROM Statistiques s
JOIN `Match` m ON s.id_match = m.id_match
JOIN Phase p ON m.id_phase = p.id_phase
JOIN Tournoi t ON p.id_tournoi = t.id_tournoi
JOIN Equipe e ON (
    e.id_equipe = m.id_equipe_1 OR e.id_equipe = m.id_equipe_2
)
GROUP BY t.id_tournoi, e.id_equipe;

-- R11 : joueurs jamais en LAN
SELECT j.pseudo
FROM Joueurs j
WHERE NOT EXISTS (
    SELECT 1
    FROM Statistiques s
    JOIN `Match` m ON s.id_match = m.id_match
    JOIN Phase p ON m.id_phase = p.id_phase
    JOIN Tournoi t ON p.id_tournoi = t.id_tournoi
    WHERE t.type = 'LAN'
    AND s.id_joueur = j.id_joueur
);

-- R12 : équipes invaincues en phase de groupe
SELECT e.nom_equipe
FROM Equipe e
WHERE NOT EXISTS (
    SELECT 1
    FROM `Match` m
    JOIN Phase p ON m.id_phase = p.id_phase
    WHERE p.nom_phase = 'Phase de groupes'
      AND (m.id_equipe_1 = e.id_equipe OR m.id_equipe_2 = e.id_equipe)
      AND m.resultat <> 'Equipe 1'
);

-- R13 : classement complet
SELECT e.nom_equipe,
       SUM(
           CASE 
               WHEN (m.resultat = 'Equipe 1' AND e.id_equipe = m.id_equipe_1) THEN 1
               WHEN (m.resultat = 'Equipe 2' AND e.id_equipe = m.id_equipe_2) THEN 1
               ELSE 0
           END
       ) AS victoires
FROM Equipe e
JOIN `Match` m 
  ON e.id_equipe = m.id_equipe_1 
  OR e.id_equipe = m.id_equipe_2
GROUP BY e.id_equipe
ORDER BY victoires DESC;

-- R14 : joueurs multi-jeux
SELECT j.pseudo
FROM Joueurs j
JOIN Roster r ON j.id_joueur = r.id_joueur
GROUP BY j.id_joueur
HAVING COUNT(DISTINCT r.id_jeu) >= 2;

-- R15 : meilleur joueur par jeu
SELECT x.id_jeu, x.pseudo, x.moyenne_score
FROM (
    SELECT r.id_jeu,
           j.pseudo,
           AVG(s.score) AS moyenne_score,
           RANK() OVER (PARTITION BY r.id_jeu ORDER BY AVG(s.score) DESC) AS rnk
    FROM Statistiques s
    JOIN Joueurs j ON s.id_joueur = j.id_joueur
    JOIN Roster r ON r.id_joueur = j.id_joueur
    GROUP BY r.id_jeu, j.id_joueur
) x
WHERE x.rnk = 1;