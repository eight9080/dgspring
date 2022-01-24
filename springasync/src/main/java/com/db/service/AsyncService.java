package com.db.service;

import java.util.concurrent.CompletableFuture;

import com.db.config.AsyncConfiguration;
import com.db.model.EmployeeAddresses;
import com.db.model.EmployeeNames;
import com.db.model.EmployeePhone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AsyncService {

	private static Logger log = LoggerFactory.getLogger(AsyncService.class);

	@Autowired
	private RestTemplate restTemplate;

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

//	@Async("asyncExecutor")
	public CompletableFuture<EmployeeNames> getEmployeeName() throws InterruptedException
	{
		return  CompletableFuture.supplyAsync(() -> {
			log.info("getEmployeeName Starts");
			EmployeeNames employeeNameData = restTemplate.getForObject("http://localhost:8082/names", EmployeeNames.class);

			log.info("employeeNameData, {}", employeeNameData);
			try {
				Thread.sleep(1000L);	//Intentional delay
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			log.info("employeeNameData completed");
			return employeeNameData;
//			return CompletableFuture.completedFuture(employeeNameData);
		}, AsyncConfiguration.COMMON_EXEC);

	}

	@Async("asyncExecutor")
	public CompletableFuture<EmployeeAddresses> getEmployeeAddress() throws InterruptedException
	{
		log.info("getEmployeeAddress Starts");
		EmployeeAddresses employeeAddressData = restTemplate.getForObject("http://localhost:8082/addresses", EmployeeAddresses.class);

		log.info("employeeAddressData, {}", employeeAddressData);
		Thread.sleep(1000L);	//Intentional delay
		log.info("employeeAddressData completed");
		return CompletableFuture.completedFuture(employeeAddressData);
	}

//	@Async("asyncExecutor")
	public CompletableFuture<EmployeePhone> getEmployeePhone() throws InterruptedException
	{
		log.info("getEmployeePhone Starts");
		EmployeePhone employeePhoneData = restTemplate.getForObject("http://localhost:8082/phones", EmployeePhone.class);

		log.info("employeePhoneData, {}", employeePhoneData);
		Thread.sleep(1000L);	//Intentional delay
		log.info("employeePhoneData completed");
		return CompletableFuture.completedFuture(employeePhoneData);
	}

}
