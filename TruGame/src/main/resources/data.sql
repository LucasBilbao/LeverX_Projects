DELETE
  FROM COMMENTS;
DELETE
  FROM GAMES;
DELETE
  FROM USERS;


-- USERS (1000 - 1002)
INSERT INTO USERS (id, firstName, lastName, password, email, role, is_approved, createdAt)
VALUES (1000, 'John',  'Doe',     '$2a$10$M4rfctMUIml7ldSmyV02oe.dNoYw88pKacLHQBCbKdRkC5zXKXC3O', 'john.doe@example.com',    'ADMIN',     TRUE, NOW()),
       (1001, 'Alice', 'Smith',   '$2a$10$M4rfctMUIml7ldSmyV02oe.dNoYw88pKacLHQBCbKdRkC5zXKXC3O', 'alice.smith@example.com', 'SELLER',    TRUE, NOW()),
       (1002, 'Bob',   'Johnson', '$2a$10$M4rfctMUIml7ldSmyV02oe.dNoYw88pKacLHQBCbKdRkC5zXKXC3O', 'bob.johnson@example.com', 'ANONYMOUS', TRUE, NOW());

-- GAMES (3000 - 3003)
INSERT INTO GAMES (id, title, text, user_id, createdAt, updatedAt)
VALUES (3000, 'Dragon Quest',   'A fantasy RPG adventure',         1000, NOW(), NOW()),
       (3001, 'Space Invaders', 'Classic alien shooter remake',    1001, NOW(), NOW()),
       (3002, 'Mystic Forest',  'Explore the enchanted woods',     1001, NOW(), NOW()),
       (3003, 'Puzzle Master',  'Solve challenging logic puzzles', 1002, NOW(), NOW());

-- COMMENTS (2000 - 2004)
INSERT INTO COMMENTS (id, message, author_id, game_id, rating, is_approved, createdAt)
VALUES (2000, 'Great platform!',        1000, 3000, 1, TRUE, NOW()),
       (2001, 'Nice game selection!',   1001, 3001, 2, TRUE, NOW()),
       (2002, 'I love this app',        1001, 3002, 3, TRUE, NOW()),
       (2003, 'Could use more reviews', 1002, 3003, 4, TRUE, NOW()),
       (2004, 'Amazing UI!',            1000, 3000, 5, TRUE, NOW());
