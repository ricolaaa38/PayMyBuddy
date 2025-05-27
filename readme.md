# Pay My Buddy

## Description

Pay My Buddy est une application permettant à des utilisateurs de transférer de l'argent à des relations en toute simplicité.

---

### Connexion sécurisée à la base de données

L'application utilise des variables d’environnement pour gérer la connexion à la base de données de manière sécurisée.
Ces variables sont à définir directement dans votre environnement système (voir ./.env.example).

---

### Modèle Physique de Données (MPD) pour MySQL

```sql
CREATE TABLE User (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(32) NOT NULL,
    email VARCHAR(128) UNIQUE NOT NULL,
    password VARCHAR(256) NOT NULL,
    solde DOUBLE DEFAULT 0 NOT NULL
);

CREATE TABLE Transaction (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    date DATETIME,
    description TEXT,
    amount DOUBLE NOT NULL,
    FOREIGN KEY (sender_id) REFERENCES User(id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES User(id) ON DELETE CASCADE
);

CREATE TABLE User_Connection (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    user2_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE,
    FOREIGN KEY (user2_id) REFERENCES User(id) ON DELETE CASCADE
);
