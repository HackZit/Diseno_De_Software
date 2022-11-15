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
    valor double NOT NULL,
    userid int NOT NULL,
    cardid int NOT NULL,
    descripcion VARCHAR(50) NOT NULL,
    PRIMARY KEY (idfactura),
    FOREIGN KEY (cardid) REFERENCES Payment_Methods (cardid)
);

CREATE TABLE IF NOT EXISTS car_data(
    carid int NOT NULL,
    userid int NOT NULL,
    licenseplate VARCHAR(50) NOT NULL,
    carbrand VARCHAR(50) NOT NULL,
    typeofcar VARCHAR(50) NOT NULL,
    carmodel VARCHAR(50) NOT NULL,
    PRIMARY KEY (carid),
    FOREIGN KEY (userid) REFERENCES users (userid)
);

CREATE TABLE IF NOT EXISTS parking_spot(
    spotid int NOT NULL,
    carid int NOT NULL,
    parktime time NOT NULL,
    parkinghistoryid int NOT NULL,
    parkingsid int NOT NULL,
    PRIMARY KEY (spotid),
    FOREIGN KEY (carid) REFERENCES car_data (carid),
    FOREIGN KEY (parkingsid) REFERENCES parking_details (parkingsid)
);

CREATE TABLE IF NOT EXISTS Payment_Methods(
    userid int NOT NULL,
    cardholder VARCHAR(50) NOT NULL,
    cardnumber int NOT NULL,
    expirationdate date NOT NULL,
    cvv int NOT NULL,
    cardtype VARCHAR(50) NOT NULL,
    billingaddress VARCHAR(50) NOT NULL,
    cellphonenumber int NOT NULL,
    cardid int NOT NULL,
    PRIMARY KEY (cardid),
    FOREIGN KEY (userid) REFERENCES users (userid)
);

CREATE TABLE IF NOT EXISTS order_history(
    idfactura int NOT NULL,
    userid int NOT NULL,
    fecha date NOT NULL,
    valor double NOT NULL,
    parkinghistoryid int NOT NULL,
    FOREIGN KEY (idfactura) REFERENCES factura (idfactura),
    FOREIGN KEY (userid) REFERENCES users (userid)
);

CREATE TABLE IF NOT EXISTS parking_history(
    parkinghistoryid int NOT NULL,
    spotid int NOT NULL,
    userid int NOT NULL,
    parkingsid int NOT NULL,
    PRIMARY KEY (parkinghistoryid)
    FOREIGN KEY (spotid) REFERENCES parking_spot (spotid),
    FOREIGN KEY (parkingsid) REFERENCES parking_details (parkingsid),
    FOREIGN KEY (userid) REFERENCES users (userid)
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