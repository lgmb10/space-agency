package com.lgambier.spaceagency.repositories;

import com.lgambier.spaceagency.enums.ShipStatus;
import com.lgambier.spaceagency.models.Ship;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class ShipRepositoryTest {

    private Ship testShip;

    @Autowired
    private ShipRepository shipRepository;

    @BeforeEach
    public void setup(){
        testShip = new Ship();
        testShip.setCapacity(10);
        testShip.setName("ship");
        testShip.setStatus(ShipStatus.RETIRED);
        testShip.setCapacity(300);

    }

    @AfterEach
    public void tearDown() {
        shipRepository.delete(testShip);
    }

    @Test
    void givenShip_thenSaved_thenCanBeFoundById(){
        Ship savedShip = shipRepository.findById(testShip.getId()).orElse(null);
        assertNotNull(savedShip);
        assertEquals(testShip.getName(), savedShip.getName());
        assertEquals(testShip.getStatus(), savedShip.getStatus());
    }

}
