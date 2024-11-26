package com.dragonshoe.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dragonshoe.entity.CartItem;
import com.dragonshoe.entity.Order;
import com.dragonshoe.entity.OrderItem;
import com.dragonshoe.entity.ProductDetail;
import com.dragonshoe.entity.User;
import com.dragonshoe.repository.CartItemRepository;
import com.dragonshoe.repository.OrderItemRepository;
import com.dragonshoe.repository.OrderRepository;
import com.dragonshoe.repository.ProductDetailRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Transactional
    public Order placeOrder(User user) {
        
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        order.setStatus("Processing"); 
        
       
        List<CartItem> cartItems = cartItemRepository.findByUser(user);

       
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            ProductDetail productDetail = cartItem.getProductDetail();
            int orderedQuantity = cartItem.getQuantity();
            
           
            if (productDetail.getStockQty() < orderedQuantity) {
                throw new IllegalStateException("Not enough stock for product: " + productDetail.getProduct().getProductName());
            }

           
            productDetail.setStockQty(productDetail.getStockQty() - orderedQuantity);
            productDetailRepository.save(productDetail);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductDetail(productDetail);
            orderItem.setQuantity(orderedQuantity);
            orderItem.setPrice(productDetail.getPrice()); 

            orderItems.add(orderItem);
        }

      
        order.setOrderItems(new HashSet<>(orderItems));

     
        BigDecimal totalAmount = calculateTotalAmount(orderItems);
        order.setTotalAmount(totalAmount);

     
        order = orderRepository.save(order);

       
        cartItemRepository.deleteAll(cartItems);

        return order;
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItems) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem orderItem : orderItems) {
            BigDecimal itemTotal = orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
            total = total.add(itemTotal);
        }
        return total;
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }
    
    public BigDecimal calculateTotalPriceForToday(LocalDate today) {
        List<Order> todaysOrders = orderRepository.findByOrderDate(today);
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Order order : todaysOrders) {
            totalPrice = totalPrice.add(order.getTotalAmount());
        }
        return totalPrice;
    }
    
    public BigDecimal calculateTotalPriceForDate(LocalDate date) {
        return orderRepository.sumTotalAmountByOrderDate(date).orElse(BigDecimal.ZERO);
    }
    public BigDecimal calculateTotalPriceForMonth(YearMonth month) {
        return orderRepository.sumTotalAmountByOrderMonth(month.atDay(1), month.atEndOfMonth()).orElse(BigDecimal.ZERO);
    }
    
    

}
