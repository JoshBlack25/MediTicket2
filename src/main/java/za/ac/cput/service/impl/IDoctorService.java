package za.ac.cput.service.impl;

import za.ac.cput.domain.user.Doctor;

import java.util.Optional;

public interface IDoctorService extends IService <Doctor,Integer> {

    Optional<Doctor> findByEmail(String email);

}

