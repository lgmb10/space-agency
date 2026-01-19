package com.lgambier.spaceagency.controllers;

import com.lgambier.spaceagency.dto.mission.request.MissionCreateRequestDTO;
import com.lgambier.spaceagency.dto.mission.request.MissionUpdateRequestDTO;
import com.lgambier.spaceagency.models.Mission;
import com.lgambier.spaceagency.services.MissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    private final JsonMapper jsonMapper;

    @GetMapping("/missions")
    public List<Mission> getAllMissions() {
        return missionService.findAll();
    }

    @GetMapping("/missions/{missionId}")
    public Mission getOneMission(@PathVariable int  missionId) {
        return missionService.findById(missionId);
    }

    @PostMapping("/missions")
    @ResponseStatus(HttpStatus.CREATED)
    public Mission createMission(@RequestBody MissionCreateRequestDTO mission) {
        return missionService.create(mission);
    }

    @PutMapping("/missions")
    public Mission updateMission(@RequestBody MissionUpdateRequestDTO mission) {
        return missionService.update(mission);
    }

//    @PatchMapping("/missions/{missionId}")
//    public Mission patchEmployee(@PathVariable Integer missionId, @RequestBody Map<String, Object> patchPayload){
//        Mission mission = missionService.findById(missionId);
//
//        if(mission == null){
//            throw new RuntimeException("Employee id not found - "+missionId);
//        }
//
//        if(patchPayload.containsKey("id")){
//            throw new RuntimeException("Employee id not allowed in request body - "+ missionId);
//        }
//
//        Mission patchedMission = jsonMapper.updateValue(mission, patchPayload);
//
//        return missionService.update(patchedMission);
//
//    }

    @DeleteMapping("/missions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMission(@PathVariable("id") Integer id){
        missionService.deleteById(id);
    }


}
