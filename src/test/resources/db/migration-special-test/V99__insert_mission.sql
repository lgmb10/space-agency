INSERT INTO `spaceagency`.`ship` (`id`, `name`, `capacity`, `max_weight`, `status`)
VALUES ('1', 'ship-test', '5', '300', 'ACTIVE');

INSERT INTO `spaceagency`.`mission` (`id`, `ship_id`, `departure_date`, `arrival_date`, `origin`, `destination`,
                                     `status`, `max_passengers`)
VALUES ('1', '1', NOW(), (NOW() + INTERVAL 5 HOUR), 'Mars', 'Neptune', 'PLANNED', '5');



