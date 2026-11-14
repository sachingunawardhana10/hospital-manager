
package com.example.hospitalmanager.repository;

import com.example.hospitalmanager.model.MedicalRecord;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    List<MedicalRecord> findAllByPatient_Id(Long patientId);

    boolean existsByPatient_Id(Long patientId);

}
