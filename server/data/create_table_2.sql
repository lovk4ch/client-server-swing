CREATE TABLE IF NOT EXISTS `mydb`.`ticket` (
  `ticketId` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `cost` INT(10) UNSIGNED NULL DEFAULT NULL,
  `passengerFio` VARCHAR(70) NOT NULL,
  `passportSeries` INT(11) NOT NULL,
  `passportNumber` INT(11) NOT NULL,
  PRIMARY KEY (`ticketId`),
  UNIQUE INDEX `ticketId_UNIQUE` (`ticketId` ASC),
  CONSTRAINT `flightId`
    FOREIGN KEY (`ticketId`)
    REFERENCES `mydb`.`flight` (`flightId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;