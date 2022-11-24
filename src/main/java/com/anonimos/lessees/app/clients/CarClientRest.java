package com.anonimos.lessees.app.clients;

import com.anonimos.lessees.app.resources.Car;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-cars", url = "ec2-184-72-157-182.compute-1.amazonaws.com:8001/api/car")
public interface CarClientRest {
    @GetMapping("/{id}")
    Car getById(@PathVariable Long id);
    @PostMapping(value = "/")
    Car create(@RequestBody Car car);
    @GetMapping(value="/cars-by-lessor")
    List<Car> getCarsByRent(@RequestParam Iterable<Long> ids);
}
