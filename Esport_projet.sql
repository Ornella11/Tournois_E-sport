-- Création de la base de donnée
CREATE DATABASE IF NOT EXISTS esport;
USE esport;

-- Suppression des tables si elles existent
DROP TABLE IF EXISTS Statistiques;
DROP TABLE IF EXISTS Roster;
DROP TABLE IF EXISTS `Match`;
DROP TABLE IF EXISTS Phase;
DROP TABLE IF EXISTS Tournoi;
DROP TABLE IF EXISTS Jeu;
DROP TABLE IF EXISTS Equipe;
DROP TABLE IF EXISTS Joueurs;
DROP TABLE IF EXISTS Editeur;

-- Création des tables
CREATE TABLE Editeur (
    id_editeur INT AUTO_INCREMENT,
    nom_editeur VARCHAR(50) NOT NULL,
    PRIMARY KEY (id_editeur)
) ENGINE=InnoDB;

CREATE TABLE Joueurs (
    id_joueur INT AUTO_INCREMENT,
    pseudo VARCHAR(50) NOT NULL,
    nom_joueur VARCHAR(50) NOT NULL,
    prenom_joueur VARCHAR(50) NOT NULL,
    date_naissance DATE NOT NULL,
    nationalite VARCHAR(50) NOT NULL,
    niveau_elo INT NOT NULL CHECK (niveau_elo >= 0),
    PRIMARY KEY (id_joueur),
    CONSTRAINT uk_joueur_pseudo UNIQUE (pseudo)
) ENGINE=InnoDB;

CREATE TABLE Equipe (
    id_equipe INT AUTO_INCREMENT,
    nom_equipe VARCHAR(50) NOT NULL,
    logo VARCHAR(255),
    date_creation DATE NOT NULL,
    pays_origine VARCHAR(50) NOT NULL,
    PRIMARY KEY (id_equipe),
    CONSTRAINT uk_equipe_nom UNIQUE (nom_equipe)
) ENGINE=InnoDB;


CREATE TABLE Jeu (
    id_jeu INT AUTO_INCREMENT,
    nom_jeu VARCHAR(50) NOT NULL,
    genre ENUM('FPS', 'MOBA', 'Sport', 'Battle Royale', 'Autre') NOT NULL,
    annee_de_sortie YEAR,
    id_editeur INT NOT NULL,
    PRIMARY KEY (id_jeu),
    CONSTRAINT uk_jeu_nom UNIQUE (nom_jeu),
    CONSTRAINT fk_jeu_editeur
        FOREIGN KEY (id_editeur)
        REFERENCES Editeur(id_editeur)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE Tournoi (
    id_tournoi INT AUTO_INCREMENT,
    nom_tournoi VARCHAR(100) NOT NULL,
    date_debut_tournoi DATE NOT NULL,
    date_fin_tournoi DATE NOT NULL,
    type ENUM('En ligne', 'LAN') NOT NULL,
    dotation DECIMAL(10,2) NOT NULL
        CHECK (dotation >= 0),
    statut ENUM('À venir', 'En cours', 'Terminé') NOT NULL,
    id_jeu INT NOT NULL,
    PRIMARY KEY (id_tournoi),
    CONSTRAINT chk_dates_tournoi
        CHECK (date_fin_tournoi >= date_debut_tournoi),
    CONSTRAINT fk_tournoi_jeu
        FOREIGN KEY (id_jeu)
        REFERENCES Jeu(id_jeu)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE Phase (
    id_phase INT AUTO_INCREMENT,
    nom_phase VARCHAR(50) NOT NULL,
    id_tournoi INT NOT NULL,
    PRIMARY KEY (id_phase),
    CONSTRAINT fk_phase_tournoi
        FOREIGN KEY (id_tournoi)
        REFERENCES Tournoi(id_tournoi)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE `Match` (
    id_match INT AUTO_INCREMENT,
    date_match DATETIME NOT NULL,

    score_equipe1 INT DEFAULT 0
        CHECK (score_equipe1 >= 0),

    score_equipe2 INT DEFAULT 0
        CHECK (score_equipe2 >= 0),

    resultat VARCHAR(50),

    nombre_maps INT DEFAULT 1
        CHECK (nombre_maps > 0),

    id_phase INT NOT NULL,
    id_equipe_1 INT NOT NULL,
    id_equipe_2 INT NOT NULL,

    PRIMARY KEY (id_match),

    CONSTRAINT chk_equipes_differentes
        CHECK (id_equipe_1 <> id_equipe_2),
    CONSTRAINT fk_match_phase
        FOREIGN KEY (id_phase)
        REFERENCES Phase(id_phase)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_match_equipe1
        FOREIGN KEY (id_equipe_1)
        REFERENCES Equipe(id_equipe)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_match_equipe2
        FOREIGN KEY (id_equipe_2)
        REFERENCES Equipe(id_equipe)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;


CREATE TABLE Roster (
    id_jeu INT,
    id_joueur INT,
    id_equipe INT,
    date_debut DATE NOT NULL,
    date_fin DATE,
    PRIMARY KEY (id_jeu, id_joueur, id_equipe, date_debut),

    CONSTRAINT chk_dates_roster
        CHECK (
            date_fin IS NULL
            OR date_fin >= date_debut
        ),

    CONSTRAINT fk_roster_jeu
        FOREIGN KEY (id_jeu)
        REFERENCES Jeu(id_jeu)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_roster_joueur
        FOREIGN KEY (id_joueur)
        REFERENCES Joueurs(id_joueur)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_roster_equipe
        FOREIGN KEY (id_equipe)
        REFERENCES Equipe(id_equipe)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE Statistiques (
    id_joueur INT,
    id_match INT,
    nb_kills INT DEFAULT 0
        CHECK (nb_kills >= 0),

    nb_deaths INT DEFAULT 0
        CHECK (nb_deaths >= 0),

    nb_assists INT DEFAULT 0
        CHECK (nb_assists >= 0),

    score INT DEFAULT 0
        CHECK (score >= 0),

    PRIMARY KEY (id_joueur, id_match),
    CONSTRAINT fk_stats_joueur
        FOREIGN KEY (id_joueur)
        REFERENCES Joueurs(id_joueur)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_stats_match
        FOREIGN KEY (id_match)
        REFERENCES `Match`(id_match)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;