package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.enums.StaffRole;
import za.ac.cput.domain.user.ClinicStaff;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClinicStaffRepository extends JpaRepository<ClinicStaff, Integer> {

    List<ClinicStaff> findByDepartment(String department);

    List<ClinicStaff> findByStaffRole(StaffRole staffRole);

    Optional<ClinicStaff> findByEmail(String email);
}