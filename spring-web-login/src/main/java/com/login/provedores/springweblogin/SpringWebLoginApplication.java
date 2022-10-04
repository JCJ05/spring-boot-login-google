package com.login.provedores.springweblogin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SpringWebLoginApplication implements CommandLineRunner {

	@Autowired
    private BCryptPasswordEncoder encoder;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringWebLoginApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		String contra = "12345";
		encoder.encode(contra);	
		
	}

}
