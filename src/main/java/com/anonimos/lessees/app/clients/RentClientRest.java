package com.anonimos.lessees.app.clients;

import com.anonimos.lessees.app.resources.Rent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(name = "msvc-rents", url = "ec2-3-86-52-51.compute-1.amazonaws.com:8004/api/rent")
public interface RentClientRest {
    @GetMapping("/{id}")
    Rent getById(@PathVariable Long id);
    @PostMapping(value = "/")
    Rent create(@RequestBody Rent car);
    @GetMapping(value="/rents-by-lessee")
    List<Rent> getRentsByLessee(@RequestParam Iterable<Long> ids);
}
