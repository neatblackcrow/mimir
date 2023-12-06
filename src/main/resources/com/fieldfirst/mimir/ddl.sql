CREATE TABLE IF NOT EXISTS NeuralNetworkWeights (
    weight DOUBLE NOT NULL
);

CREATE TABLE IF NOT EXISTS UserCases (
    created_on                      DATETIME DEFAULT CURRENT_TIMESTAMP,

    last_predicted_interval         DOUBLE NOT NULL,
    review_interval                 DOUBLE NOT NULL,
    repetition                      DOUBLE NOT NULL,
    grade                           DOUBLE NOT NULL,
    predicted_interval              DOUBLE NOT NULL,

    PRIMARY KEY (created_on),
    CHECK ((last_predicted_interval >= 0.0 AND last_predicted_interval <= 1.0) AND
        (review_interval >= 0.0 AND review_interval <= 1.0) AND
        (repetition >= 0.0 AND repetition <= 1.0) AND
        (grade >= 0.0 AND grade <= 1.0) AND
        (predicted_interval >= 0.0 AND predicted_interval <= 1.0))
);

CREATE TABLE IF NOT EXISTS CardTypes (
    name                            TEXT,

    PRIMARY KEY (name)
);

CREATE TABLE IF NOT EXISTS Categories (
    id                              INTEGER,
    name                            TEXT NOT NULL,
    created_on                      DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_on                      DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    parent_category                 INTEGER DEFAULT 1 NOT NULL,
    ordered                         INTEGER NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (parent_category) REFERENCES Categories (id) ON DELETE NO ACTION,
    CHECK (updated_on >= created_on),
    CHECK (ordered > 0)
);

CREATE TABLE IF NOT EXISTS Cards (
    id                              INTEGER,
    created_on                      DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_on                      DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,

    last_predicted_interval         INTEGER NOT NULL,
    review_interval                 INTEGER NOT NULL,
    repetition                      INTEGER NOT NULL,
    grade                           INTEGER NOT NULL,
    predicted_interval              INTEGER NOT NULL,

    front                           TEXT NOT NULL,
    back                            TEXT,
    next_review_on                  DATE NOT NULL,
    last_review_on                  DATE DEFAULT CURRENT_DATE NOT NULL,
    category                        INTEGER DEFAULT 1 NOT NULL,
    card_type                       TEXT NOT NULL,
    ordered                         INTEGER NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (card_type) REFERENCES CardTypes (name) ON UPDATE CASCADE ON DELETE NO ACTION,
    FOREIGN KEY (category) REFERENCES Categories (id) ON DELETE NO ACTION,
    CHECK ((last_predicted_interval >= 0 AND last_predicted_interval <= 2048) AND
        (review_interval >= 0 AND review_interval <= 2048) AND
        (repetition >= 0 AND repetition <= 128) AND
        (grade >= 0 AND grade <= 5) AND
        (predicted_interval >= 0 AND predicted_interval <= 2048)),
    CHECK (updated_on >= created_on),
    CHECK (ordered > 0)
);

INSERT INTO CardTypes VALUES ('Deliberate practice'), ('Incremental reading'), ('Fact');
INSERT INTO Categories VALUES (1, 'Root', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 1);