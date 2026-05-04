package com.lgambier.spaceagency.actuator;


import com.lgambier.spaceagency.services.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class InProgressMissionsMetric implements InfoContributor {

    private final MissionService missionService;

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("inProgressMissions", missionService.countInProgressMissions());
    }
}