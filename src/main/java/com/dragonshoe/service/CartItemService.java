package com.dragonshoe.service;



import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dragonshoe.entity.CartItem;
import com.dragonshoe.entity.Product;
import com.dragonshoe.entity.ProductDetail;
import com.dragonshoe.entity.User;
import com.dragonshoe.repository.CartItemRepository;

@Service
public class CartItemService {
	
	@Autowired
    private CartItemRepository cartItemRepository;

	public CartItem saveCartItem(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }
	
	public CartItem findCartItemById(Long cartItemId) {
        return cartItemRepository.findById(cartItemId).orElse(null);
    }
	
	public List<CartItem> getCartItemsByUserId(Long userId) {
        return cartItemRepository.findByUserUserId(userId);
    }
	public int getTotalQuantityByUserId(Long userId) {
        Integer totalQuantity = cartItemRepository.sumQuantityByUserId(userId);
        return totalQuantity != null ? totalQuantity.intValue() : 0;
    }
	public void deleteCartItemsByUser(User user) {
        cartItemRepository.deleteByUser(user);
    }
	public void deleteCartItemById(long cartItemId) {
		cartItemRepository.deleteById(cartItemId);
    }
	public CartItem findCartItemByUserAndProductDetail(User user, ProductDetail productDetail) {
	    return cartItemRepository.findByUserAndProductDetail(user, productDetail);
	}

}