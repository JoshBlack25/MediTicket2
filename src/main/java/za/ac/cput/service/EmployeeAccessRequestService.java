package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.EmployeeAccessRequest;
import za.ac.cput.domain.enums.RequestStatus;
import za.ac.cput.repository.EmployeeAccessRequestRepository;
import za.ac.cput.service.impl.IEmployeeAccessRequestService;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeAccessRequestService implements IEmployeeAccessRequestService {

    @Autowired
    private final EmployeeAccessRequestRepository repository;

    public EmployeeAccessRequestService(EmployeeAccessRequestRepository repository) {
        this.repository = repository;
    }

    @Override
    public EmployeeAccessRequest create(EmployeeAccessRequest request) {
        if (request == null) return null;
        return repository.save(request);
    }

    @Override
    public EmployeeAccessRequest read(Integer requestId) {
        return repository.findById(requestId).orElse(null);
    }

    @Override
    public EmployeeAccessRequest update(EmployeeAccessRequest request) {
        if (request == null) return null;

        EmployeeAccessRequest existing = read(request.getRequestId());
        if (existing == null) return null;

        // Re-apply every field explicitly rather than blindly overwriting —
        // same pattern used to fix the PatientService/DoctorService update() bug.
        EmployeeAccessRequest updated = new EmployeeAccessRequest.Builder()
                .copy(existing)
                .setEmail(request.getEmail())
                .setRequestedUserType(request.getRequestedUserType())
                .setRequestedStaffRole(request.getRequestedStaffRole())
                .setStatus(request.getStatus())
                .setRequestDate(request.getRequestDate())
                .setProcessedDate(request.getProcessedDate())
                .setProcessedBy(request.getProcessedBy())
                .setAdminNotes(request.getAdminNotes())
                .build();

        return repository.save(updated);
    }

    @Override
    public void delete(Integer requestId) {
        repository.deleteById(requestId);
    }

    @Override
    public List<EmployeeAccessRequest> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<EmployeeAccessRequest> findByEmailAndStatus(String email, RequestStatus status) {
        return repository.findByEmailAndStatus(email, status);
    }

    @Override
    public List<EmployeeAccessRequest> findByStatus(RequestStatus status) {
        return repository.findByStatus(status);
    }
}