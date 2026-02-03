INSERT INTO `spaceagency`.`ship` (`id`, `name`, `capacity`, `max_weight`, `status`)
VALUES ('1', 'ship-test', '5', '300', 'ACTIVE');

INSERT INTO `spaceagency`.`mission` (`id`, `ship_id`, `departure_date`, `arrival_date`, `origin`, `destination`,
                                     `status`, `max_passengers`)
VALUES ('1', '1', NOW(), (NOW() + INTERVAL 5 HOUR), 'Mars', 'Neptune', 'PLANNED', '5');

INSERT INTO `spaceagency`.`passenger` (`id`, `first_name`, `last_name`, `email`, `weight`, `medical_clearance`) VALUES ('1', 'astronaut', 'test', 'astronaut@user.com', '80', 1);

INSERT INTO `spaceagency`.`passenger` (`id`, `first_name`, `last_name`, `email`, `weight`, `medical_clearance`) VALUES ('2', 'astronaut', 'test2', 'astronaut2@test.com', '80', 1);

