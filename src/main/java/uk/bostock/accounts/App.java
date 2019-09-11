package uk.bostock.accounts;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class App {
	public static void main(String[] args) {
		Gson gson = new Gson();

		Map<String, Account> store = new HashMap<>();

		post("/accounts", (req, res) -> {
			NewAccountArgs arg = gson.fromJson(req.body(), NewAccountArgs.class);
			if (args == null) {
				res.status(400);
				res.type("text/plain");
				return "400 Bad Request";
			}

			Account account = new Account(arg.initial_balance);
			store.put(account.getAccountNumber(), account);

			res.status(201);
			res.type("text/json");
			return "{account_number: "+account.getAccountNumber()+"}";
		});

		get("/account/:number", (req, res) -> {
			String number = req.params(":number");
			Account account = store.get(number);

			if (account == null) {
				res.status(404);
				res.type("text/plain");
				return "404 Not Found";
			} else {
				res.status(200);
				res.type("text/json");
				return "{balance: "+account.getBalance()+"}";
			}
		});

		put("/account/:number", (req, res) -> {
			res.type("text/plain");

		        String number = req.params(":number");
		        Account src = store.get(number);

		        TransferArgs arg = gson.fromJson(req.body(), TransferArgs.class);
		        if (arg == null || number.equals(arg.other_account)) {
			        res.status(400);
			        return "400 Bad Request";
		        }

		        Account dest = store.get(arg.other_account);

		        if (src == null || dest == null) {
			        res.status(404);
			        return "404 Not Found";
		        }

		        try {
			        src.transferTo(dest, arg.value);
                                res.status(200);
                                return "200 OK";
		        } catch (InsufficientBalanceException e) {
			        res.status(409);
			        return "409 Conflict";
		        }
		});
	}
}

class NewAccountArgs {
	int initial_balance;
}

class TransferArgs {
	String other_account;
	int value;
}
