package com.example.demo.controller;

import java.time.Instant;

import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class RestController {
	
	@PostMapping(path = "/print", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONObject> greeting(@RequestBody JSONObject input) {
		
		String message = (String) input.get("message");
		System.out.println(Instant.now().toString()+" - "+message);
		JSONObject response  = new JSONObject();
		response.put("status", "success");
		return new ResponseEntity<JSONObject>(response, HttpStatus.OK);
	}

}
