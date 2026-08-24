package za.ac.cput.service.impl;

import za.ac.cput.domain.enums.StaffRole;
import za.ac.cput.domain.user.ClinicStaff;

import java.util.List;
import java.util.Optional;

public interface IClinicStaffService extends IService<ClinicStaff,Integer> {

    Optional<ClinicStaff> findByEmail(String email);

    List<ClinicStaff> findByDepartment(String department);

    List<ClinicStaff> findByStaffRole(StaffRole staffRole);
}