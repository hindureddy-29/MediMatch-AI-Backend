package com.medimatch.repository;

import com.medimatch.model.Medicine;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MedicineRepository {

    private final Map<Long, Medicine> medicineStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(100);

    public List<Medicine> findAll() {
        return new ArrayList<>(medicineStore.values());
    }

    public Optional<Medicine> findById(Long id) {
        return Optional.ofNullable(medicineStore.get(id));
    }

    public Medicine save(Medicine medicine) {
        if (medicine.getId() == null || medicine.getId() == 0) {
            medicine.setId(idGenerator.incrementAndGet());
        }
        medicineStore.put(medicine.getId(), medicine);
        return medicine;
    }

    public void saveAll(Collection<Medicine> medicines) {
        for (Medicine m : medicines) {
            save(m);
        }
    }

    public boolean existsById(Long id) {
        return medicineStore.containsKey(id);
    }

    public void deleteById(Long id) {
        medicineStore.remove(id);
    }

    public long count() {
        return medicineStore.size();
    }
}
