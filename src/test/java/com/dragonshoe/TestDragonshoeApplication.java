package com.dragonshoe;

import org.springframework.boot.SpringApplication;

public class TestDragonshoeApplication {

	public static void main(String[] args) {
		SpringApplication.from(DragonshoeApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
