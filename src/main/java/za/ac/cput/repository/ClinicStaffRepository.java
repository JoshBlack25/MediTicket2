package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.user.ClinicStaff;

import java.util.List;

@Repository
public interface ClinicStaffRepository extends JpaRepository<ClinicStaff, Integer> {

    List<ClinicStaff> findByDepartment(String department);

    List<ClinicStaff> findByStaffRole(String staffRole);

    ClinicStaff findByEmail(String email);
}