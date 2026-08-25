package za.ac.cput.service.impl;

import za.ac.cput.domain.EmployeeAccessRequest;
import za.ac.cput.domain.enums.RequestStatus;

import java.util.List;
import java.util.Optional;

public interface IEmployeeAccessRequestService extends IService<EmployeeAccessRequest, Integer> {

    Optional<EmployeeAccessRequest> findByEmailAndStatus(String email, RequestStatus status);

    List<EmployeeAccessRequest> findByStatus(RequestStatus status);
}