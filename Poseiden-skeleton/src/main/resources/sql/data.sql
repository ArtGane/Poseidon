-- Insert four users, one is admin other's not
-- Pass : Poseidon@0
INSERT INTO users (username, password, fullname, role)
VALUES ('Admin', '$2y$10$oIU/rutqzUgOzhOBMuGAUuCdwm.bSzMHIvYfGIC.voZFYzyswNoNG', 'Administratrice', 'ADMIN');
-- Pass : Jelly*4life
INSERT INTO users (username, password, fullname, role)
VALUES ('Jelly', '$2y$10$Bh2oIKKfMIvQ.bFYx3qTT.s4vn2hVIMzD6EHOR9Dib8as37ipp.Om', 'Jelly Fish', 'USER');
-- Pass : Password123
INSERT INTO users (username, password, fullname, role)
VALUES ('Shark', '$2y$10$8rcBy3B1q2kf6Ibg3D0wXeDhPjko17vgdIgbxy97kjqtmsRjznZY2', 'John Doe', 'USER');
-- Pass : Secret456
INSERT INTO users (username, password, fullname, role)
VALUES ('Pingoo', '$2y$10$saZblQhq3fOGXqQbURQbge7rMvFIGWTEP93ddeatJF9nCLJ1DRUfC', 'Alice Smith', 'USER');
-- Pass : Confidential789
INSERT INTO users (username, password, fullname, role)
VALUES ('Fish', '$2y$10$IjiXnIo4GdExCSMtPhnk2OSJHGzA75uelGsSVgV7F/vKOIIr6YXzO', 'Fish Filet', 'USER');

-- Insert five bids
INSERT INTO bid (account, type) VALUES ('account1', 'type1');
INSERT INTO bid (account, type) VALUES ('account2', 'type2');
INSERT INTO bid (account, type) VALUES ('account3', 'type3');
INSERT INTO bid (account, type) VALUES ('account4', 'type4');
INSERT INTO bid (account, type) VALUES ('account5', 'type5');

-- Insert three curves points
INSERT INTO curvepoint (curve_id, term, value) VALUES (1, 2.5, 3.5);
INSERT INTO curvepoint (curve_id, term, value) VALUES (1, 5.0, 4.2);
INSERT INTO curvepoint (curve_id, term, value) VALUES (2, 1.0, 2.8);

-- Insert four ratings
INSERT INTO rating (fitch_rating, moodys_rating) VALUES ('AAA', 'Aaa');
INSERT INTO rating (fitch_rating, moodys_rating) VALUES ('BBB', 'Baa2');
INSERT INTO rating (fitch_rating, moodys_rating) VALUES ('CC', 'Ca');
INSERT INTO rating (fitch_rating, moodys_rating) VALUES ('DDD', 'Daa');

-- Insert three rules
INSERT INTO rule (name, description) VALUES ('Rule 1', 'Description for Rule 1');
INSERT INTO rule (name, description) VALUES ('Rule 2', 'Description for Rule 2');
INSERT INTO rule (name, description) VALUES ('Rule 3', 'Description for Rule 3');

-- Insert five trade
INSERT INTO trade (account, type) VALUES ('Acc1', 'Type1');
INSERT INTO trade (account, type) VALUES ('Acc2', 'Type2');
INSERT INTO trade (account, type) VALUES ('Acc3', 'Type3');
INSERT INTO trade (account, type) VALUES ('Acc4', 'Type4');
INSERT INTO trade (account, type) VALUES ('Acc5', 'Type5');