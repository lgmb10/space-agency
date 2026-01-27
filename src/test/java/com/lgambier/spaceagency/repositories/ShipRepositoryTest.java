package com.lgambier.spaceagency.repositories;

import com.lgambier.spaceagency.config.AbstractIntegrationTest;
import com.lgambier.spaceagency.enums.ShipStatus;
import com.lgambier.spaceagency.models.Ship;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ShipRepositoryTest extends AbstractIntegrationTest {

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
