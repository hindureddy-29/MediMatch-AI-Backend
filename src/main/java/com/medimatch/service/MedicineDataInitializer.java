package com.medimatch.service;

import com.medimatch.model.Medicine;
import com.medimatch.repository.MedicineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MedicineDataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(MedicineDataInitializer.class);
    private final MedicineRepository repository;

    public MedicineDataInitializer(MedicineRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) return;

        List<Medicine> initialFormulary = List.of(
                new Medicine(1L, "Dolo 650 Tablet", "Dolo", "Paracetamol",
                        "Paracetamol 650mg", "tablet", "650mg",
                        "Micro Labs Ltd", "Analgesics / Antipyretics", "N02BE01", 33.5),

                new Medicine(2L, "Calpol 650mg Tablet", "Calpol", "Paracetamol",
                        "Paracetamol 650mg", "tablet", "650mg",
                        "GlaxoSmithKline Pharmaceuticals", "Analgesics / Antipyretics", "N02BE01", 32.8),

                new Medicine(3L, "Crocin 650 Advance Tablet", "Crocin Advance", "Paracetamol",
                        "Paracetamol 650mg", "tablet", "650mg",
                        "GlaxoSmithKline Consumer Healthcare", "Analgesics / Antipyretics", "N02BE01", 34.0),

                new Medicine(4L, "Pacimol 500 Tablet", "Pacimol", "Paracetamol",
                        "Paracetamol 500mg", "tablet", "500mg",
                        "Ipca Laboratories Ltd", "Analgesics / Antipyretics", "N02BE01", 20.0),

                new Medicine(5L, "Augmentin 625 Duo Tablet", "Augmentin Duo", "Amoxicillin + Clavulanic Acid",
                        "Amoxicillin 500mg + Clavulanic Acid 125mg", "tablet", "625mg",
                        "GlaxoSmithKline Pharmaceuticals", "Antibiotics", "J01CR02", 210.0),

                new Medicine(6L, "Amoxyclav 625 Tablet", "Amoxyclav", "Amoxicillin + Clavulanic Acid",
                        "Amoxicillin 500mg + Clavulanic Acid 125mg", "tablet", "625mg",
                        "Abbott Healthcare", "Antibiotics", "J01CR02", 198.5),

                new Medicine(7L, "Azithral 500 Tablet", "Azithral", "Azithromycin",
                        "Azithromycin 500mg", "tablet", "500mg",
                        "Alembic Pharmaceuticals Ltd", "Macrolide Antibiotics", "J01FA10", 125.0),

                new Medicine(8L, "Azee 500 Tablet", "Azee", "Azithromycin",
                        "Azithromycin 500mg", "tablet", "500mg",
                        "Cipla Ltd", "Macrolide Antibiotics", "J01FA10", 121.5),

                new Medicine(9L, "Telma 40 Tablet", "Telma", "Telmisartan",
                        "Telmisartan 40mg", "tablet", "40mg",
                        "Glenmark Pharmaceuticals", "Antihypertensives", "C09CA07", 130.0),

                new Medicine(10L, "Telmikind 40 Tablet", "Telmikind", "Telmisartan",
                        "Telmisartan 40mg", "tablet", "40mg",
                        "Mankind Pharma", "Antihypertensives", "C09CA07", 88.0),

                new Medicine(11L, "Pantocid 40 Tablet", "Pantocid", "Pantoprazole",
                        "Pantoprazole 40mg", "tablet", "40mg",
                        "Sun Pharmaceutical Industries", "Proton Pump Inhibitors", "A02BC02", 165.0),

                new Medicine(12L, "Pan 40 Tablet", "Pan", "Pantoprazole",
                        "Pantoprazole 40mg", "tablet", "40mg",
                        "Alkem Laboratories Ltd", "Proton Pump Inhibitors", "A02BC02", 155.0),

                new Medicine(13L, "Prednisone 5mg Tablet", "Prednisone", "Prednisone",
                        "Prednisone 5mg", "tablet", "5mg",
                        "Pfizer", "Corticosteroids", "H02AB07", 45.0),

                new Medicine(14L, "Prednisolone 5mg Tablet", "Prednisolone", "Prednisolone",
                        "Prednisolone 5mg", "tablet", "5mg",
                        "GSK", "Corticosteroids", "H02AB06", 48.0),

                new Medicine(15L, "Celebrex 200mg Capsule", "Celebrex", "Celecoxib",
                        "Celecoxib 200mg", "capsule", "200mg",
                        "Pfizer Inc", "NSAIDs", "M01AH01", 240.0),

                new Medicine(16L, "Celexa 20mg Tablet", "Celexa", "Citalopram",
                        "Citalopram Hydrobromide 20mg", "tablet", "20mg",
                        "AbbVie", "Antidepressants (SSRI)", "N06AB04", 220.0)
        );

        repository.saveAll(initialFormulary);
        log.info("Initialized MediMatch AI repository with {} benchmark medicines", repository.count());
    }
}
