package com.adamkaz7.medicalclinic.service;

import com.adamkaz7.medicalclinic.command.CreateDoctorCommand;
import com.adamkaz7.medicalclinic.command.UpdateDoctorCommand;
import com.adamkaz7.medicalclinic.dto.DoctorDto;
import com.adamkaz7.medicalclinic.exception.doctor.DoctorAlreadyExistsException;
import com.adamkaz7.medicalclinic.exception.doctor.DoctorNotFoundException;
import com.adamkaz7.medicalclinic.exception.facility.MedicalFacilityNotFoundException;
import com.adamkaz7.medicalclinic.mapper.DoctorMapper;
import com.adamkaz7.medicalclinic.model.Doctor;
import com.adamkaz7.medicalclinic.model.MedicalFacility;
import com.adamkaz7.medicalclinic.repository.DoctorRepository;
import com.adamkaz7.medicalclinic.repository.MedicalFacilityRepository;
import com.adamkaz7.medicalclinic.validator.DoctorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final MedicalFacilityRepository medicalFacilityRepository;
    private final DoctorValidator doctorValidator;

    public DoctorDto addDoctor(CreateDoctorCommand command) {
        doctorValidator.validateCreateDoctorCommand(command);
        if (doctorRepository.existsByEmail(command.email())) {
            throw new DoctorAlreadyExistsException(command.email());
        }
        Doctor doctor = doctorMapper.toEntity(command);
        Doctor savedDoctor = doctorRepository.save(doctor);
        return doctorMapper.toDto(savedDoctor);
    }

    public List<DoctorDto> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(doctorMapper::toDto)
                .toList();
    }

    public void assignDoctorToMedicalFacility(Long doctorId, Long facilityId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException(doctorId));
        MedicalFacility facility = medicalFacilityRepository.findById(facilityId)
                .orElseThrow(() -> new MedicalFacilityNotFoundException(facilityId));
        boolean alreadyAssigned = medicalFacilityRepository
                .existsByIdAndDoctors_Id(facilityId, doctorId);
        doctorValidator.validateMedicalFacilityAssignment(alreadyAssigned);
        facility.assignDoctor(doctor);
        medicalFacilityRepository.save(facility);
    }

    public DoctorDto getDoctorById(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException(doctorId));
        return doctorMapper.toDto(doctor);
    }

    public void changeDoctorPassword(Long doctorId, String newPassword) {
        doctorValidator.validatePassword(newPassword);
        Doctor doctor = findDoctorById(doctorId);
        doctor.changePassword(newPassword);
        doctorRepository.save(doctor);
    }

    public DoctorDto updateDoctorById(Long doctorId, UpdateDoctorCommand command) {
        doctorValidator.validateUpdateDoctorCommand(command);
        Doctor doctor = findDoctorById(doctorId);
        if (!doctor.getEmail().equals(command.email()) && doctorRepository.existsByEmail(command.email())) {
            throw new DoctorAlreadyExistsException(command.email());
        }
        doctor.updateDoctor(command);
        Doctor savedDoctor = doctorRepository.save(doctor);
        return doctorMapper.toDto(savedDoctor);
    }

    public void deleteDoctor(Long doctorId) {
        Doctor doctor = findDoctorById(doctorId);
        doctorRepository.delete(doctor);
    }

    private Doctor findDoctorById(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException(doctorId));
    }
}
