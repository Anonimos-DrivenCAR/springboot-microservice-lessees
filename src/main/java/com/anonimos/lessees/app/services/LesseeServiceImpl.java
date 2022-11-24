package com.anonimos.lessees.app.services;

import com.anonimos.lessees.app.clients.CarClientRest;
import com.anonimos.lessees.app.clients.RentClientRest;
import com.anonimos.lessees.app.model.entity.Lessee;
import com.anonimos.lessees.app.model.entity.LesseeRent;
import com.anonimos.lessees.app.repository.LesseeRepository;
import com.anonimos.lessees.app.resources.Car;
import com.anonimos.lessees.app.resources.Rent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LesseeServiceImpl implements LesseeService {

    @Autowired
    private LesseeRepository lesseeRepository;
    @Autowired
    private RentClientRest clientRest;

    @Autowired
    private CarClientRest clientRestC;

    @Transactional(readOnly = true)
    @Override
    public List<Lessee> findAll() {
        return (List<Lessee>) lesseeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Lessee> findLessorById(Long id) {
        return lesseeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existByEmail(String email) {
        return lesseeRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Lessee> findByEmail(String email) {
        return lesseeRepository.getByEmail(email);
    }

    @Override
    @Transactional
    public Lessee create(Lessee newLessor) {
        return lesseeRepository.save(newLessor);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        lesseeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Lessee update(Long id, Lessee newL) {
        return lesseeRepository.findById(id)
                .map(
                        l->{
                            l.setName(newL.getName());
                            l.setEmail(newL.getEmail());
                            l.setUsername(newL.getUsername());
                            l.setPassword(newL.getPassword());
                            return lesseeRepository.save(l);
                        }
                ).get();
    }

    /**Microservices-iterations*/
    @Override
    @Transactional(readOnly = true)
    public Optional<Lessee> findByIdWithCars(Long id) {
        Optional<Lessee> o = lesseeRepository.findById(id);
        if(o.isPresent()){
            Lessee lessee = o.get();
            if(!lessee.getLesseeRents().isEmpty()){
                List<Long> ids = lessee.getLesseeRents().stream().map(
                        lesseeRent -> lesseeRent.getRentId()).collect(Collectors.toList());


                List<Rent> rents = clientRest.getRentsByLessee(ids);
                List<Car> cars = (List<Car>) clientRestC.getById(id);
                 lessee.setRents(rents);
                lessee.setCars(cars);

            }
            return Optional.of(lessee);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void deleteLessorCarById(Long id) {
        lesseeRepository.deleteLesseeRentByID(id);
    }

    @Override
    @Transactional
    public Optional<Rent> assignCar(Rent rent, Long lesseeId) {

        Optional<Lessee> o = lesseeRepository.findById(lesseeId);

        if(o.isPresent()){
            Rent carMsvc = clientRest.getById(rent.getId());
            Lessee lessee= o.get();
            LesseeRent lesseeRent = new LesseeRent();
            lesseeRent.setRentId(carMsvc.getId());

            lessee.addLesseeRent(lesseeRent);
            lesseeRepository.save(lessee);

            return Optional.of(carMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Rent> createCar(Rent rent, Long lesseeId) {
        Optional<Lessee> o = lesseeRepository.findById(lesseeId);
        if(o.isPresent()){
            Rent newCarMsvc = clientRest.create(rent);

            Lessee lessor= o.get();
            LesseeRent lessorCar = new LesseeRent();
            lessorCar.setRentId(newCarMsvc.getId());

            lessor.addLesseeRent(lessorCar);
            lesseeRepository.save(lessor);
            return Optional.of(newCarMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Rent> unAssignCar(Rent rent, Long lesseeId) {
        Optional<Lessee> o = lesseeRepository.findById(lesseeId);
        if(o.isPresent()){
            Rent carMsvc = clientRest.getById(rent.getId());

            Lessee lessee= o.get();
            LesseeRent lesseeRent = new LesseeRent();
            lesseeRent.setRentId(carMsvc.getId());

            lessee.removeLesseeRent(lesseeRent);
            lesseeRepository.save(lessee);

            return Optional.of(carMsvc);
        }
        return Optional.empty();
    }
}
