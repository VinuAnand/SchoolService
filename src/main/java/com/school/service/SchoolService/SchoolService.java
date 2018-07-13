package com.school.service.SchoolService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.beans.Student;

@RestController
@RequestMapping(value="/school-service")
public class SchoolService {
	@Autowired
    RestTemplate restTemplate;
	
	@GetMapping(value = "/getStudentListBySchool/{schoolName}")
	public ResponseEntity<List<Student>> getStudentListBySchool(@PathVariable("schoolName") String schoolName){
		System.out.println("Getting  details for " + schoolName);
		
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        
		ResponseEntity<List<Student>> response = restTemplate.exchange("http://student-service/student-service/getStudentDetailsForSchool/{schoolName}",HttpMethod.GET, entity,new ParameterizedTypeReference<List<Student>>() {},"abcschool");
        //ResponseEntity<List<Student>> response = restTemplate.exchange("http://localhost:8098/student-service/getStudentDetailsForSchool/{schoolName}",HttpMethod.GET, entity,new ParameterizedTypeReference<List<Student>>() {},"abcschool");
        
		
		return new ResponseEntity<List<Student>>(response.getBody(),HttpStatus.OK);
	}
	
	@GetMapping(value = "/addToStudentListBySchool/{schoolName}")
	public ResponseEntity<Void> addToStudentListBySchool(@PathVariable("schoolName") String schoolName){
		System.out.println("Add  details for " + schoolName);
		
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        Student student = new Student("Vinu","12th");
        
        HttpEntity<Student> entity = new HttpEntity(student,headers);
        
		ResponseEntity<Void> response = restTemplate.exchange("http://student-service/student-service/addStudentForSchool/{schoolName}",HttpMethod.POST, entity,Void.class,"abcschool");
		//restTemplate.postForObject("http://student-service/student-service/addStudentForSchool/{schoolName}", student, Void.class,"abcschool");
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	
	@Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
