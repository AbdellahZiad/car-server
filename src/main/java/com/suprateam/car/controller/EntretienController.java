package com.suprateam.car.controller;

import com.suprateam.car.model.EntretienAndFix;
import com.suprateam.car.service.impl.EntretienServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/entretien")
public class EntretienController {


    @Autowired
    private EntretienServiceImpl entretienService;


    @ApiOperation(value = "Filter data")
    @GetMapping("/search")
    public ResponseEntity<?> filterSurveyUserMobil(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC, page = 0) Pageable pageable,
                                                   @RequestParam String filter) {
        return ResponseEntity.ok(entretienService.filter(pageable, filter));
    }

    @ApiOperation(value = "Add Entretien")
    @PostMapping("/add/{id}")
    ResponseEntity<?> addEntretien(@RequestBody EntretienAndFix userDto,@PathVariable Long id) {
        return ResponseEntity.ok(entretienService.saveEntretien(userDto,id));
    }


    @ApiOperation(value = "Delete User")
    @DeleteMapping("/delete/{id}")
    ResponseEntity<?> deleteEntretien(@PathVariable Long id) {
        return new ResponseEntity<String>(entretienService.deleteEntretien(id), HttpStatus.OK);

    }


    @ApiOperation(value = "Get All Entretien")
    @GetMapping("")
    ResponseEntity<?> getAllEntretien() {
        return ResponseEntity.ok(entretienService.getAllEntretien());
    }

//    @GetMapping(value = "/export")
//    public ResponseEntity<?> exportTemplates() throws IOException {
////        return surveyUserService.exportTemplates(response);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Disposition", "attachment; filename=" + entretienService.generateExcelReportName());
//
//        return ResponseEntity
//                .ok()
//                .headers(headers)
//                .body(entretienService.exportTemplates());
//    }

}
