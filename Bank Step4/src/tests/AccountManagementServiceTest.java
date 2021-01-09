package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Set;

import org.junit.jupiter.api.Test;

import accounting.Account;
import accounting.AccountManagementService;
import accounting.Customer;
import credit.CreditService;
import valueObjects.AccountNumber;
import valueObjects.Amount;

class AccountManagementServiceTest {

	public static AccountManagementService prepareTestData(CreditService cs) {
		AccountManagementService ams = new AccountManagementService(cs);
		Customer customer = ams.newCustomer("Carola", "Lilienthal",LocalDate.of(1967, 9, 11));
		ams.newAccount(new Amount(1000), customer);
		ams.newAccount(new Amount(5000), customer);
		ams.newAccount(new Amount(2000),customer);
		

		customer = ams.newCustomer("Hans", "Lilienthal",LocalDate.of(1968, 9, 11));
		ams.newAccount(new Amount(2000),customer);
		ams.newAccount(new Amount(5000),customer);
		
		customer = ams.newCustomer("Dieter", "Lilienthal",LocalDate.of(1969, 9, 11));
		ams.newAccount(new Amount(3000),customer);
		ams.newAccount(new Amount(5000),customer);
		
		customer = ams.newCustomer("Franz", "Lilienthal",LocalDate.of(1964, 9, 11));
		ams.newAccount(new Amount(4000),customer);
		ams.newAccount(new Amount(5000),customer);
		
		customer = ams.newCustomer("Carsten", "Lilienthal",LocalDate.of(1965, 9, 11));
		ams.newAccount(new Amount(5000),customer);

		return ams;
	}
	
	@Test
	void testAMSCreation() {	
		CreditService cs = new CreditService();
		AccountManagementService ams = AccountManagementServiceTest.prepareTestData(cs);
		assertNotNull(ams.getAccountList());
		assertNotNull(ams.getCustomerList());
		assertEquals(5,ams.getCustomerList().size());
		assertEquals(10,ams.getAccountList().size());
		int counter = 0;
		for (Customer customer : ams.getCustomerList()) {
			counter = counter + customer.getAccountList().size();	
		}
		assertEquals(10, counter);
		assertEquals(5,cs.getCreditCustomerList().size());
		assertEquals(0,cs.getCreditAccountList().size());
	}
	
	@Test
	void testAMSTransferMoney() {		
		AccountManagementService ams = AccountManagementServiceTest.prepareTestData(new CreditService());

		Set<AccountNumber> accountNumbers = ams.getAccountNumberList();
		Iterator<AccountNumber> iterator = accountNumbers.iterator();
		AccountNumber debitorAccountNumber = iterator.next();
		AccountNumber creditorAccountNumber = iterator.next();
		Amount debitorSaldo = ams.getAccount(debitorAccountNumber).getBalance();
		Amount creditorSaldo = ams.getAccount(creditorAccountNumber).getBalance();
		ams.transferMoney(new Amount(100), debitorAccountNumber, creditorAccountNumber);
		assertEquals(debitorSaldo.subtract(new Amount(100)), ams.getAccount(debitorAccountNumber).getBalance());
		assertEquals(creditorSaldo.add(new Amount(100)), ams.getAccount(creditorAccountNumber).getBalance());
		
	}
	
	@Test
	void testAMSNewCustomerNewAccount() {
		CreditService cs = new CreditService();
		AccountManagementService ams = AccountManagementServiceTest.prepareTestData(cs);
		
		Customer newCustomer = ams.newCustomer("Tea", "Ginster", LocalDate.of(1950, 12, 2));
		assertTrue(ams.getCustomerList().contains(newCustomer));
		assertEquals(6,ams.getCustomerList().size());
		assertEquals(6,cs.getCreditCustomerList().size());

		Account newAccount = ams.newAccount(new Amount(2000), newCustomer);
		assertTrue(ams.getAccountList().contains(newAccount));
		assertEquals(newAccount, ams.getAccount(newAccount.getAccountnumber()));
		assertTrue(newCustomer.getAccountList().contains(newAccount));
		assertEquals(11,ams.getAccountList().size());
		
		assertTrue(ams.getAccountNumberList().contains(newAccount.getAccountnumber()));
		
	}

}