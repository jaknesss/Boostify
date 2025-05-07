package com.fra.boostify;

import com.fra.boostify.role.Role;
import com.fra.boostify.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
public class BoostifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoostifyApplication.class, args);
		//SpringApplication.run(BoostifyApplication.class, "--debug");
	}

	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository){
		return args -> {
			if(roleRepository.findByName("USER").isEmpty()) roleRepository.save(Role.builder().name("USER").build());
		};
	}
}
