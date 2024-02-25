USE keycloak_db;
CREATE TABLE IF NOT EXISTS chess_games (
    id INT AUTO_INCREMENT PRIMARY KEY,
    moves TEXT,
    fen TEXT, 
    winner VARCHAR(40),
	color VARCHAR(5),
	elo VARCHAR(4), 
	username_id VARCHAR(36),
    game_id VARCHAR(68),
    wins  SMALLINT UNSIGNED,
    losses SMALLINT UNSIGNED,
    draws SMALLINT UNSIGNED
);
