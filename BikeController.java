package com.example.demo.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Bike;
import com.example.demo.service.BikeService;

@RestController
@RequestMapping("/bikes")
public class BikeController {

	@Autowired
	BikeService Bikeservice;

	@GetMapping("")	
	public List<Bike> list() {
		return Bikeservice.ListAllBike();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Bike> get(@PathVariable Integer id) {
		try {
			Bike bike = Bikeservice.getBike(id);
			return new ResponseEntity<Bike>(bike, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<Bike>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping
	public void add(@RequestBody Bike bike) {
		Bikeservice.saveBike(bike);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Bike> update(@RequestBody Bike bike, @PathVariable Integer id)

	{
		try {
			Bike Exitbike = Bikeservice.getBike(id);
			bike.setId(id);
			Bikeservice.saveBike(bike);
			return new ResponseEntity<Bike>(bike, HttpStatus.OK);

		} catch (NoSuchElementException e) {
			return new ResponseEntity<Bike>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Integer id) {
		Bikeservice.deleteBike(id);
	}

}
