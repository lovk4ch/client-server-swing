CREATE TABLE IF NOT EXISTS `mydb`.`flight` (
  `flightId` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `flightCode` INT(10) UNSIGNED NOT NULL,
  `dispatchDate` DATETIME NOT NULL,
  `dispatchCity` VARCHAR(45) NOT NULL,
  `targetDate` DATETIME NOT NULL,
  `targetCity` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`flightId`, `flightCode`),
  UNIQUE INDEX `flightId_UNIQUE` (`flightId` ASC),
  UNIQUE INDEX `flightCode_UNIQUE` (`flightCode` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;