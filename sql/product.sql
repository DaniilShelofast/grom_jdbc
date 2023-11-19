
CREATE TABLE product (
    ID BIGINT AUTO_INCREMENT NOT NULL,
    CONSTRAINT product_pk PRIMARY KEY (ID),
    NAME NVARCHAR(50) NOT NULL,
    DESCRIPTION LONGTEXT,
    PRICE INT NOT NULL
);

