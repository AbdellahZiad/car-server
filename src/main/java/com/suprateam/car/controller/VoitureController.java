package com.suprateam.car.controller;

import com.suprateam.car.dto.FilterDto;
import com.suprateam.car.model.Voiture;
import com.suprateam.car.service.impl.VoitureServiceImpl;
import com.suprateam.car.util.VoitureStatus;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/voiture")
public class VoitureController {


    @Autowired
    private VoitureServiceImpl voitureService;


    @ApiOperation(value = "Filter data")
    @GetMapping("/search")
    public ResponseEntity<?> filterSurveyVoitureMobil(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC, page = 0) Pageable pageable,
                                                      @RequestParam String filter) {
        return ResponseEntity.ok(voitureService.filter(pageable, filter));
    }

    @ApiOperation(value = "Add Voiture")
    @PostMapping("/add")
    ResponseEntity<?> addVoiture(@RequestBody Voiture VoitureDto) {
        return ResponseEntity.ok(voitureService.saveVoiture(VoitureDto));
    }

    @ApiOperation(value = "Add Voiture")
    @PostMapping("/search-adv")
    ResponseEntity<?> filterAdv(@RequestBody FilterDto filterDto) {
        return ResponseEntity.ok(voitureService.searchAdv(filterDto));
    }


    @ApiOperation(value = "Delete Voiture")
    @DeleteMapping("/delete/{id}")
    ResponseEntity<?> deleteVoiture(@PathVariable Long id) {
        return new ResponseEntity<String>(voitureService.deleteVoiture(id), HttpStatus.OK);

    }


    @ApiOperation(value = "Get All Voiture")
    @GetMapping("")
    ResponseEntity<?> getAllVoiture() {
        return ResponseEntity.ok(voitureService.getAllVoiture());
    }


    @ApiOperation(value = "Get All Voiture")
    @GetMapping("/details/{id}")
    ResponseEntity<?> getDetailsVoiture(@PathVariable Long id) {
        return ResponseEntity.ok(voitureService.getDetailsVoitures(id));
    }


    @GetMapping(value = "/status")
    public VoitureStatus getStatus() {
        return voitureService.getStatus();
    }


}
