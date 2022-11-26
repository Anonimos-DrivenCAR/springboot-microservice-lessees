package com.anonimos.lessees.app.controllers;

import com.anonimos.lessees.app.model.entity.Lessee;
import com.anonimos.lessees.app.resources.Rent;
import com.anonimos.lessees.app.services.LesseeService;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping(value = "/api/lessee")
public class LesseeController {
   private final LesseeService service;

   public LesseeController(LesseeService service) {
        this.service = service;
   }

    @GetMapping("/all")
    public Map<String,List<Lessee>> getAll() {
        return Collections.singletonMap("lessees",service.findAll());
    }


    @GetMapping("/details/{id}") //getByID -> Details
    public ResponseEntity<?> detail(@PathVariable Long id){
        Optional<Lessee> o = service.findByIdWithCars(id);     //service.findLessorById(id);
        if(o.isPresent()){
            return ResponseEntity.ok(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long  id){
        Optional<Lessee> carOptional = service.findLessorById(id);
        if(carOptional.isPresent()){
            return ResponseEntity.ok(carOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?>delete(@PathVariable Long id) {
        Optional<Lessee> l = service.findLessorById(id);
        if(l.isPresent()){
            service.delete(id);
            return ResponseEntity.noContent().build();
        }
        return  ResponseEntity.notFound().build();
    }


    @PostMapping("/")
    public ResponseEntity<?> create(@Valid @RequestBody Lessee lessee, BindingResult result){
        if(result.hasErrors()){
            return validate(result);
        }
        if(!lessee.getEmail().isEmpty() &&  service.existByEmail(lessee.getEmail())){
            return ResponseEntity.badRequest().
                    body(Collections.
                            singletonMap("error", "There is already a user with that email address!"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(lessee));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Lessee lessee, BindingResult result, @PathVariable Long id){

        if(result.hasErrors()){
            return validate(result);
        }
        Optional<Lessee> l = service.findLessorById(id);
        if (l.isPresent()){
            Lessee lessorDb = l.get();
            if(!lessee.getEmail().isEmpty()
                    && !lessee.getEmail().equalsIgnoreCase(lessorDb.getEmail())
                    && service.findByEmail(lessee.getEmail()).isPresent()){
                return ResponseEntity.badRequest().
                        body(Collections.
                                singletonMap("error", "There is already a user with that email address!"));
            }
            return new ResponseEntity<>(service.update(id,lessee),HttpStatus.CREATED);
        }
        return ResponseEntity.notFound().build();
    }

    /**Microservices Iteration*/

    @PutMapping( "/assign-car/{lessorId}")
    public ResponseEntity<?> assignCar(@RequestBody Rent rent, @PathVariable Long lessorId){
        Optional<Rent> o ;
        try {
            o = service.assignCar(rent,lessorId);
        }catch (FeignException exception ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message","Does not exist car by id or communication error: " +
                    exception.getMessage()));
        }

        if(o.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();

    }

    @PostMapping("/create-car/{lessorId}")
    public ResponseEntity<?> createCar(@RequestBody Rent rent,  @PathVariable Long lessorId){
        Optional<Rent> o ;
        try {
            o = service.createCar(rent,lessorId);
        }catch (FeignException exception ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message","Can't create the car or communication error : " +
                    exception.getMessage()));
        }
        if(o.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/unAssign-car/{lessorId}")
    public ResponseEntity<?> deleteCar(@RequestBody Rent rent,  @PathVariable Long lessorId){
        Optional<Rent> o ;
        try {
            o = service.unAssignCar(rent,lessorId);
        }catch (FeignException exception ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message","Can't create the car or communication error : " +
                    exception.getMessage()));
        }
        if(o.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete_car/{id}")
    public ResponseEntity<?> deleteLessorCar(@PathVariable Long id){
        service.deleteLessorCarById(id);
        return ResponseEntity.noContent().build();
    }


    /**validation*/
    private static ResponseEntity<Map<String, String>> validate(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err ->{
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
