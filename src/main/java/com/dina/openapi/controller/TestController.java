package com.dina.openapi.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/test")
@Tag(description = "test", name = "test")
public class TestController {

	@GetMapping("/get-data")
	@Operation(description = "test data")
	@Cacheable(value = "itemCache")
	public String test(@RequestHeader(name = "test-header", required = true) String testHeader) {
		System.out.println("Header received " + testHeader);
		return "get data";
	}

}
