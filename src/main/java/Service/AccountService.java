package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.*;

public class AccountService {
    public AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account addUser(Account account) {
            if((account.getUsername().length() == 0) || (account.getPassword().length() < 4) || (accountDAO.getAccount(account) != null)) {
                return null;
            } else {
                return accountDAO.addUser(account);
            }
    }

    public Account getUser(Account account) {
        return accountDAO.getAccount(account);
    }

    public Account updatePassword(Account account) {
        return null;
    }

    public Account loginValidate(Account account) {
        if (accountDAO.loginValidate(account) != null) {
            return accountDAO.loginValidate(account);
        } else {
            return null;
        }
        // Account existingAccount = accountDAO.getAccountByUsername(account.getUsername());
    
        // if (existingAccount != null && existingAccount.getPassword().equals(account.getPassword())) {
        //     return existingAccount;
        // }
        
        // return null;
    }

}
