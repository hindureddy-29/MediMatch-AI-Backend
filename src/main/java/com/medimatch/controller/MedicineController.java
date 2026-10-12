package com.medimatch.controller;

import com.medimatch.dto.CompareRequestDTO;
import com.medimatch.dto.ComparisonReportDTO;
import com.medimatch.dto.MatchRequestDTO;
import com.medimatch.dto.MatchResultDTO;
import com.medimatch.model.Medicine;
import com.medimatch.repository.MedicineRepository;
import com.medimatch.service.DuplicateDetectionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    private final MedicineRepository medicineRepository;
    private final DuplicateDetectionService duplicateDetectionService;

    public MedicineController(MedicineRepository medicineRepository,
                              DuplicateDetectionService duplicateDetectionService) {
        this.medicineRepository = medicineRepository;
        this.duplicateDetectionService = duplicateDetectionService;
    }

    /**
     * GET /api/medicines
     * Retrieve all medicines registered in the formulary
     */
    @GetMapping
    public ResponseEntity<List<Medicine>> getAllMedicines() {
        return ResponseEntity.ok(medicineRepository.findAll());
    }

    /**
     * POST /api/medicines
     * Register a new medicine in the formulary
     */
    @PostMapping
    public ResponseEntity<Medicine> addMedicine(@RequestBody Medicine medicine) {
        Medicine saved = medicineRepository.save(medicine);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * POST /api/medicines/match
     * Match a query medicine against the repository for potential duplicates & LASA hazards
     */
    @PostMapping("/match")
    public ResponseEntity<List<MatchResultDTO>> searchDuplicates(@RequestBody MatchRequestDTO request) {
        Object query = request.getQuery();
        if (query == null) {
            return ResponseEntity.badRequest().build();
        }

        int threshold = request.getThreshold();
        List<Medicine> allMedicines = medicineRepository.findAll();

        List<MatchResultDTO> results = allMedicines.stream()
                .map(med -> {
                    ComparisonReportDTO report = duplicateDetectionService.evaluatePair(query, med);
                    int overallScore = report.getScores() != null ? report.getScores().getOverallScore() : 0;
                    return new MatchResultDTO(med, report, overallScore);
                })
                .filter(res -> res.getOverallScore() >= threshold || "CRITICAL".equalsIgnoreCase(res.getEvaluation().getRiskLevel()))
                .sorted((a, b) -> Integer.compare(b.getOverallScore(), a.getOverallScore()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(results);
    }

    /**
     * POST /api/medicines/compare
     * Perform deep side-by-side comparison between two arbitrary medicines
     */
    @PostMapping("/compare")
    public ResponseEntity<ComparisonReportDTO> compareMedicines(@RequestBody CompareRequestDTO request) {
        Object medA = request.getMedicineA();
        Object medB = request.getMedicineB();

        if (medA == null || medB == null) {
            return ResponseEntity.badRequest().build();
        }

        ComparisonReportDTO report = duplicateDetectionService.evaluatePair(medA, medB);
        return ResponseEntity.ok(report);
    }
}
