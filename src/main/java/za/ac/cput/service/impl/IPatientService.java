package za.ac.cput.service.impl;

import za.ac.cput.domain.user.Patient;

import java.time.LocalDate;
import java.util.List;

public interface IPatientService extends IService <Patient,Integer > {

    Patient findByEmail(String email);

    List<Patient> findByDateRegistered(LocalDate dateRegistered);


}