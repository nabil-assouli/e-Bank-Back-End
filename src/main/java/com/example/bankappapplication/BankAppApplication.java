package com.example.bankappapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankAppApplication {

//	private final CustomerRepository customerRepository;
//
//	public BankAppApplication(CustomerRepository customerRepository) {
//		this.customerRepository = customerRepository;
//	}

	public static void main(String[] args) {
		SpringApplication.run(BankAppApplication.class, args);
	}

//	@Bean
//	CommandLineRunner start(CustomerRepository CustomerRepository,
//							AccountRepository AccountRepository,
//							OperationRepository OperationRepository, AccountRepository accountRepository, OperationRepository operationRepository){
//
//		return args -> {
//			Stream.of("Nabil_1", "Nabil_2", "Nabil_3").forEach(name -> {
//				Customer customer = new Customer();
//				customer.setName(name);
//				customer.setEmail(name+"@gmail.com");
//				customerRepository.save(customer);
//			});
//			customerRepository.findAll().forEach(customer -> {
//				Account account = new Account();
//				account.setId(UUID.randomUUID().toString());
//				account.setBalance(Math.random()*100);
//				account.setCreatedAt(new Date());
//				account.setCustomer(customer);
//				account.setOverDraft(9000);
//				AccountRepository.save(account);
//			});
//			accountRepository.findAll().forEach(account -> {
//				Operation operation = new Operation();
//				operation.setOperationDate(new Date());
//				operation.setAmount(Math.random()*1000);
//				operation.setType(Math.random()>0.5? OperationType.DEPOT: OperationType.RETRAIT);
//				operation.setAccount(account);
//				operationRepository.save(operation);
//			});
//		};
//	}

}
