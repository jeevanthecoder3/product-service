package com.greenry.productservice.Controllers;

import com.greenry.productservice.Dao.CartRepository;
import com.greenry.productservice.Dao.ProductRepository;
import com.greenry.productservice.Dao.UserRepository;
import com.greenry.productservice.Entities.Address;
import com.greenry.productservice.Entities.Cart;
import com.greenry.productservice.Entities.Product;
import com.greenry.productservice.Entities.User;
import com.greenry.productservice.Service.CartService;
import com.greenry.productservice.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/{userName}")
public class CartController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/{ptitle}/addProducts-to-cart")
    public ResponseEntity<Cart> addProductToCart(@PathVariable("userName")String userName,@PathVariable("ptitle")String ptitle){
        if((userName==null || ptitle==null) || userService.checkValidity(userName)==false){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }else {
            User user = userRepository.findUserByUserName(userName);
            Product product = productRepository.findProductByPTitle(ptitle);
            if (user == null || product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                Cart cart = cartService.addCart(user, product);
                return ResponseEntity.ok(cart);
            }
        }

    }

    @GetMapping("/set-cart-address/{fullName}")
    public ResponseEntity<Cart> setAddress(@PathVariable("userName")String userName,@PathVariable("fullName")String fullName){
        if((userName==null || fullName==null) || userService.checkValidity(userName)==false){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }else {
            User user = userRepository.findUserByUserName(userName);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            } else {
                Set<Address> address = user.getAddress();
                for (Address address1 : address) {
                    if (address1.getFullName().equals(fullName)) {
                        user.getCart().setAddress(address1);

                        return ResponseEntity.ok(cartRepository.save(user.getCart()));
                    }
                }
            }
        }
return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
