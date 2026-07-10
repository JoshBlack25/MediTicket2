package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.user.ClinicStaff;
import za.ac.cput.repository.ClinicStaffRepository;
import za.ac.cput.service.impl.IClinicStaffService;

import java.util.List;

@Service
public class ClinicStaffService implements IClinicStaffService {

    private ClinicStaffRepository clinicStaffRepository;

    @Autowired
    public ClinicStaffService(ClinicStaffRepository clinicStaffRepository) {
        this.clinicStaffRepository = clinicStaffRepository;
    }

    @Override
    public ClinicStaff create(ClinicStaff clinicStaff) {
        return clinicStaffRepository.save(clinicStaff);
    }

    @Override
    public ClinicStaff read(Integer id) {
        return clinicStaffRepository.findById(id).get();
    }

   @Override
    public ClinicStaff update(ClinicStaff clinicStaff) {

        ClinicStaff existing = this.clinicStaffRepository
            .findById(clinicStaff.getUserId())
            .orElse(null);

        if (existing == null) {
        return null;
    }

    ClinicStaff updated = new ClinicStaff.Builder()
            .copy(existing)
            .setStaffRole(clinicStaff.getStaffRole())
            .setDepartment(clinicStaff.getDepartment())
            .build();

    return clinicStaffRepository.save(updated);
}

    @Override
    public void delete(Integer id) {
        clinicStaffRepository.deleteById(id);
    }

    @Override
    public List<ClinicStaff> getAll() {
        return clinicStaffRepository.findAll();
    }

    @Override
    public ClinicStaff findByEmail(String email) {
        return clinicStaffRepository.findByEmail(email);
    }

    @Override
    public List<ClinicStaff> findByDepartment(String department) {
        return clinicStaffRepository.findByDepartment(department);
    }

    @Override
    public List<ClinicStaff> findByStaffRole(String staffRole) {
        return clinicStaffRepository.findByStaffRole(staffRole);
    }
}