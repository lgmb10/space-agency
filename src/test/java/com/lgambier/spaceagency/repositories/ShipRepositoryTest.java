package com.lgambier.spaceagency.repositories;

import com.lgambier.spaceagency.config.TestConfiguration;
import com.lgambier.spaceagency.enums.ShipStatus;
import com.lgambier.spaceagency.models.Ship;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

//@DataJpaTest(properties = {"spring.main.web-application-type=none", "spring.security.enabled=false", "spring.flyway.enabled=true"})

@Import(TestConfiguration.class)
public class ShipRepositoryTest {

    @Autowired
    private ShipRepository shipRepository;

    private Ship testShip;

    @BeforeEach
    public void setup() {
        testShip = new Ship();
        testShip.setCapacity(10);
        testShip.setName("ship");
        testShip.setStatus(ShipStatus.RETIRED);
        testShip.setMaxWeight(300);
    }

    @AfterEach
    public void tearDown() {
        shipRepository.deleteAll();
    }

    @Test
    void givenShip_thenSaved_thenPersisted() {
        Ship savedShip = shipRepository.save(testShip);
        assertNotNull(savedShip);
        assertEquals(testShip.getName(), savedShip.getName());
        assertEquals(testShip.getStatus(), savedShip.getStatus());
    }

}
