package com.operations.StageOps;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StageOpsApplication {

	public static void main(String[] args) {
//		SpringApplication.run(StageOpsApplication.class, args);
		Application.launch(JavafxApplication.class, args);
	}

}
