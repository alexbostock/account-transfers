package uk.bostock.accounts;

public class Account {
	// Account balance in pence/cents
	// Assume the same currency unit is used throughout the application
	private int balance;

	// Each account has a unique, immutable account number
	private final int number;
	// Account numbers are exposed to users as 8-digit strings
	private final String prettyNumber;

	// Account numbers are allocated sequentially, for simplicity
        private static int nextAccountNumber = 0;

        private static synchronized int allocateAccountNumber() {
	        return nextAccountNumber++;
        }

	/**
	 * @return a new account with a unique account number
	 * @param initialBalance the account's starting balance
	 */
	public Account(int initialBalance) {
		number = allocateAccountNumber();
		prettyNumber = String.format("%08d", number);
		balance = initialBalance;
	}

        /**
         * @return the current account balance
         */
	public synchronized int getBalance() {
		return balance;
	}

        /**
         * @return account number
         */
	public String getAccountNumber() {
		return prettyNumber;
	}

        /**
         * Transfer money from this account to another account
         * @param other the destination account
         * @param value the amount of money to be transferred
         */
	public void transferTo(Account other, int value) throws InsufficientBalanceException {
		// Acquire locks in order of account number to avoid deadlock
		Account first = (this.number < other.number) ? this : other;
		Account second = (this.number < other.number) ? other : this;

		synchronized (first) {
			synchronized (second) {
				if (this.balance < value) {
					throw new InsufficientBalanceException("Insufficient balance in account " + prettyNumber);
				}

				this.balance -= value;
				other.balance += value;
			}
		}
	}
}

class InsufficientBalanceException extends Exception {
	public InsufficientBalanceException(String msg) {
		super(msg);
	}
}
