CREATE TABLE IF NOT EXISTS users(
    userid int NOT NULL,
    name VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    dateofbirth date NOT NULL,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    PRIMARY KEY (userid)
);

CREATE TABLE IF NOT EXISTS factura(
    idfactura int NOT NULL,
    fecha timestamp NOT NULL,
    valor float NOT NULL,
    userid int NOT NULL,
    cardid int NOT NULL,
    descripcion VARCHAR(50) NOT NULL,
    PRIMARY KEY (idfactura)
    
);

CREATE TABLE IF NOT EXISTS car_data(
    carid int NOT NULL,
    userid int NOT NULL,
    licenseplate VARCHAR(50) NOT NULL,
    carbrand VARCHAR(50) NOT NULL,
    typeofcar VARCHAR(50) NOT NULL,
    carmodel VARCHAR(50) NOT NULL,
    PRIMARY KEY (carid)
);

CREATE TABLE IF NOT EXISTS parking_spot(
    spotid int NOT NULL,
    carid int NOT NULL,
    parktime time NOT NULL,
    parkinghistoryid int NOT NULL,
    parkingsid int NOT NULL,
    PRIMARY KEY (spotid)
);

CREATE TABLE IF NOT EXISTS payment_methods(
    userid int NOT NULL,
    cardholder VARCHAR(50) NOT NULL,
    cardnumber int NOT NULL,
    expirationdate date NOT NULL,
    cvv int NOT NULL,
    cardtype VARCHAR(50) NOT NULL,
    billingaddress VARCHAR(50) NOT NULL,
    cellphonenumber int NOT NULL,
    cardid int NOT NULL,
    PRIMARY KEY (cardid)
);

CREATE TABLE IF NOT EXISTS order_history(
    idfactura int NOT NULL,
    userid int NOT NULL,
    fecha date NOT NULL,
    valor float NOT NULL,
    parkinghistoryid int NOT NULL
);

CREATE TABLE IF NOT EXISTS parking_history(
    parkinghistoryid int NOT NULL,
    spotid int NOT NULL,
    userid int NOT NULL,
    parkingsid int NOT NULL,
    PRIMARY KEY (parkinghistoryid)

);

CREATE TABLE IF NOT EXISTS parking_details(
    parkingsid int NOT NULL,
    numberofspots int NOT NULL,
    parkinghours time NOT NULL,
    PRIMARY KEY (parkingsid)
);

CREATE TABLE IF NOT EXISTS parking_locations(
    parkingsid int NOT NULL,
    address VARCHAR(50) NOT NULL,
    contactnumber int NOT NULL,
    businessname VARCHAR(50) NOT NULL,
    PRIMARY KEY (parkingsid)
);
CREATE TABLE IF NOT EXISTS client_cloud(

);

ALTER TABLE factura
   ADD FOREIGN KEY (cardid) REFERENCES Payment_Methods (cardid);


ALTER TABLE car_data
    ADD FOREIGN KEY (userid) REFERENCES users (userid);


ALTER TABLE parking_spot
    ADD FOREIGN KEY (carid) REFERENCES car_data (carid),
    ADD FOREIGN KEY (parkingsid) REFERENCES parking_details (parkingsid);


ALTER TABLE payment_methods
    ADD FOREIGN KEY (userid) REFERENCES users (userid);


ALTER TABLE order_history
    ADD FOREIGN KEY (idfactura) REFERENCES factura (idfactura),
    ADD FOREIGN KEY (userid) REFERENCES users (userid);


ALTER TABLE parking_history
    ADD FOREIGN KEY (spotid) REFERENCES parking_spot (spotid),
    ADD FOREIGN KEY (parkingsid) REFERENCES parking_details (parkingsid),
    ADD FOREIGN KEY (userid) REFERENCES users (userid);

