package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.PatientTicket;
import za.ac.cput.repository.PatientTicketRepository;
import za.ac.cput.service.impl.IPatientTicketService;

import java.util.List;

@Service
public class PatientTicketService implements IPatientTicketService {

    @Autowired
    private PatientTicketRepository repository;

    @Override
    public PatientTicket create(PatientTicket ticket) {
        if (ticket == null) return null;
        return repository.save(ticket);
    }

    @Override
    public PatientTicket read(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public PatientTicket update(PatientTicket ticket) {
        if (ticket == null) return null;
        return repository.save(ticket);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<PatientTicket> getAll() {
        return repository.findAll();
    }
}
