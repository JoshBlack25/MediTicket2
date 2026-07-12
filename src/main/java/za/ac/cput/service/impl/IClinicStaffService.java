package za.ac.cput.service.impl;

import za.ac.cput.domain.user.ClinicStaff;

import java.util.List;

public interface IClinicStaffService extends IService<ClinicStaff,Integer> {


    ClinicStaff findByEmail(String email);

    List<ClinicStaff> findByDepartment(String department);

    List<ClinicStaff> findByStaffRole(String staffRole);
}