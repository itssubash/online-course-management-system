package com.yenyasoft.online_course_management_system.controllers;

import com.yenyasoft.online_course_management_system.dtos.ApiResponse;
import com.yenyasoft.online_course_management_system.dtos.MaterialRequest;
import com.yenyasoft.online_course_management_system.dtos.MaterialResponse;
import com.yenyasoft.online_course_management_system.services.MaterialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MaterialResponse>>> getAllMaterials() {
        return ResponseEntity.ok(ApiResponse.ok("Materials fetched successfully", materialService.getAllMaterials()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MaterialResponse>> getMaterialById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Material fetched successfully", materialService.getMaterialById(id)));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<MaterialResponse>>> getMaterialsByCourseId(@PathVariable Long courseId) {
        return ResponseEntity.ok(ApiResponse.ok("Materials fetched successfully", materialService.getMaterialsByCourseId(courseId)));
    }

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MaterialResponse>> createMaterial(@RequestBody MaterialRequest request) {
        return new ResponseEntity<>(ApiResponse.ok("Material created successfully", materialService.createMaterial(request)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MaterialResponse>> updateMaterial(@PathVariable Long id, @RequestBody MaterialRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Material updated successfully", materialService.updateMaterial(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteMaterial(@PathVariable Long id) {
        materialService.deleteMaterial(id);
        return new ResponseEntity<>(ApiResponse.ok("Material deleted successfully"), HttpStatus.NO_CONTENT);
    }
}
