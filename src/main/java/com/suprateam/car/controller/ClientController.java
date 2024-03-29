package com.suprateam.car.controller;

import com.suprateam.car.dto.ClientDto;
import com.suprateam.car.dto.UserDto;
import com.suprateam.car.model.Client;
import com.suprateam.car.service.UserService;
import com.suprateam.car.service.impl.ClientServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/client")
public class ClientController {


    @Autowired
    private ClientServiceImpl clientService;


    @ApiOperation(value = "Filter data")
    @GetMapping("/search")
    public ResponseEntity<?> filterSurveyUserMobil(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC, page = 0) Pageable pageable,
                                                   @RequestParam String filter) {
        return ResponseEntity.ok(clientService.filter(pageable,filter));
    }

    @ApiOperation(value = "Add User")
    @PostMapping("/add")
    ResponseEntity<?> addUser(@RequestBody ClientDto userDto) {
        return ResponseEntity.ok(clientService.saveClient(userDto));
    }


    @ApiOperation(value = "Delete User")
    @DeleteMapping("/delete/{id}")
    ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return new ResponseEntity<String>(clientService.deleteClient(id), HttpStatus.OK);

    }


    @ApiOperation(value = "Get All User")
    @GetMapping("")
    ResponseEntity<?> getAllUser() {
        return ResponseEntity.ok(clientService.getAllClient());
    }

    @GetMapping(value = "/export")
    public ResponseEntity<?> exportTemplates() throws IOException {
//        return surveyUserService.exportTemplates(response);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + clientService.generateExcelReportName());

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(clientService.exportTemplates());
    }

    @ApiOperation(value = "Get Details Client")
    @GetMapping("/details/{id}")
    ResponseEntity<?> getDetailsClient(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getDetailsClients(id));
    }

}
