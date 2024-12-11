package io.camunda.hu.exercise.manufacture_supplements;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


@SpringBootApplication
public class WorkerApplication {
	static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	static ConsoleHandler handler = new ConsoleHandler();
	@Autowired
	private ZeebeClient client;

	public static void main(String[] args) {
		SpringApplication.run(WorkerApplication.class, args);
		logger.setLevel(Level.ALL);
		handler.setLevel(Level.ALL);
		logger.addHandler(handler);
	}

	@JobWorker(type = "send_sample")
	public void sendSample(final ActivatedJob job) {
		Map<String, Object> variables = new HashMap<>();
        variables.put("result", "passed");
		logger.info("Send sample");
		client.newPublishMessageCommand()
		.messageName("Test results")
		.correlationKey("results-1")
		.variables(variables)
		.send()
		.join();
		logger.info(variables.toString());
	}

	@JobWorker(type = "send_mixture")
	public void sendMixture(final ActivatedJob job, @Variable int orderValue) {
		Map<String, Object> variables = new HashMap<>();
        variables.put("order", orderValue);
		logger.info("Send mixture");
		client.newPublishMessageCommand()
		.messageName("Order ready")
		.correlationKey("mixture-1")
		.variables(variables)
		.send()
		.join();
		logger.info(variables.toString());
	}

	@JobWorker(type = "order_prep")
	public void prepOrder(final ActivatedJob job) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("orderValue", 450);
		client.newCompleteCommand(job.getKey())
				.variables(variables)
				.send()
				.exceptionally( throwable -> { throw new RuntimeException("Could not complete job " + job, throwable); });
	}

	@JobWorker(type = "check_invoice")
	public void checkInvoice(final ActivatedJob job, @Variable int order, @Variable int orderValue) {
		Map<String, Object> variables = new HashMap<>();
		if (order == orderValue) {
			variables.put("invoiceStatus", "passed");
		} else {
			variables.put("invoiceStatus", "failed");
		}
        
		client.newCompleteCommand(job.getKey())
				.variables(variables)
				.send()
				.exceptionally( throwable -> { throw new RuntimeException("Could not complete job " + job, throwable); });
	}
}