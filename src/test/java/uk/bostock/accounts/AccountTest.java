package uk.bostock.accounts;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AccountTest extends TestCase {
	public AccountTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(AccountTest.class);
	}

	public void testAccountCreation() {
		Account a1 = new Account(1000);
		assertEquals(a1.getBalance(), 1000);

		Account a2 = new Account(2000);
		assertEquals(a2.getBalance(), 2000);

		assert(!a1.getAccountNumber().equals(a2.getAccountNumber()));
	}

	public void testTransfers() {
		Account a1 = new Account(2000);
		Account a2 = new Account(2000);

                try {
			a1.transferTo(a2, 500);
			assertEquals(a1.getBalance(), 1500);
			assertEquals(a2.getBalance(), 2500);

			a2.transferTo(a1, 1000);
			assertEquals(a1.getBalance(), 2500);
			assertEquals(a2.getBalance(), 1500);
                } catch (Exception e) {
	                fail("Unexpected exception from transfer");
                }

		try {
			a1.transferTo(a2, 3000);
			fail("Transfer with insufficient balance did not throw exception");
		} catch (InsufficientBalanceException e) {
			// Expected exception
		} catch (Exception e) {
			fail("Transfer with insufficient balance threw wrong exception");
		}
	}
}
