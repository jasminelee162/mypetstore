package csu.web.mypetstore.persistence;

import csu.web.mypetstore.domain.Account;
import csu.web.mypetstore.domain.Cart;
import csu.web.mypetstore.domain.CartItem;

public interface CartDao {
    public Cart getCart(String username);

    public void updateCart(Cart cart, Account account);

    public void addItemToCart(Account account, CartItem cartItem);

    public void removeCart(Account account);

    public void removeItemFromCart(Account account,String itemId);
}