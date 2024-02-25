package com.greenry.productservice.Service;

import com.greenry.productservice.Dao.CartRepository;
import com.greenry.productservice.Dao.ItemQuantityRepository;
import com.greenry.productservice.Dao.ProductRepository;
import com.greenry.productservice.Dao.UserRepository;
import com.greenry.productservice.Entities.Cart;
import com.greenry.productservice.Entities.ItemQuantity;
import com.greenry.productservice.Entities.Product;
import com.greenry.productservice.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CartService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ItemQuantityRepository itemQuantityRepository;

    public Cart addCart(User user,Product product){
        int flag=0;
        if(user!=null || product!=null) {
            Cart cart;
            if(user.getCart()==null) {
                cart = new Cart();

                user.setCart(cart);
                cart.setUser(user);
            }else{
                cart=user.getCart();
            }
            Set<ItemQuantity> itemQuantitySet = cart.getItemQuantity();
            cart.setTotalCost(cart.getTotalCost()+product.getPPrice());

            ItemQuantity itemQuantity1 = null;

            if(cart.getItemQuantity()==null){
                if(product.getQuantity()>0) {
                    ItemQuantity item = new ItemQuantity();
                    item.setProduct(product);
                    item.setQuantity(1);
                    item.setCart(cart);
                    Set<ItemQuantity> itemQuantitySet1 = new HashSet<>();
                    itemQuantitySet1.add(item);
                    cart.setItemQuantity(itemQuantitySet1);
                }else
                    throw new RuntimeException("No more Products available!!!!");
            }else
            {
                        for(ItemQuantity itemQuantity:cart.getItemQuantity()){
                            if(itemQuantity.getProduct().getPTitle().equals(product.getPTitle())){
                                if(itemQuantity.getQuantity()+1 > product.getQuantity()){
                                    throw new RuntimeException("Still more products does not exists!!!!");
                                }else
                                itemQuantity.setQuantity(itemQuantity.getQuantity()+1);
                                itemQuantity1=itemQuantity;
                                flag=1;
                            }
                        }

                if(flag==0) {
                    if(product.getQuantity()>0) {

                        itemQuantity1 = new ItemQuantity();
                        itemQuantity1.setCart(cart);
                        itemQuantity1.setQuantity(1);
                        itemQuantity1.setProduct(product);
                        if (itemQuantitySet == null) {
                            Set<ItemQuantity> i1 = new HashSet<>();
                            i1.add(itemQuantity1);
                            cart.setItemQuantity(i1);
                        } else
                            cart.getItemQuantity().add(itemQuantity1);
                    }else
                        throw new RuntimeException("No more product available!!!");
                }
                System.out.println("Adding the product");
            }
            if(flag==1){

                itemQuantityRepository.save(itemQuantity1);
            }else {
                System.out.println("Triggering save!!!!");
                cartRepository.save(cart);
            }return cart;
        }else
            throw new RuntimeException("User or Product not found !!!!");
    }

    public Cart deleteProduct(User user,Product product){
        Cart cart = user.getCart();

        ItemQuantity itemQuantity1=null;

        for(ItemQuantity itemQuantity:cart.getItemQuantity()){
            if(itemQuantity.getProduct().getPTitle().equals(product.getPTitle())){
                if(itemQuantity.getQuantity()-1 == 0){
                    itemQuantity1=itemQuantity;
                    cart.getItemQuantity().remove(itemQuantity1);
                    this.itemQuantityRepository.delete(itemQuantity1);

                }else {
                    itemQuantity.setQuantity(itemQuantity.getQuantity() - 1);
                    itemQuantity1 = itemQuantity;
                    this.itemQuantityRepository.save(itemQuantity1);
                }

            }
        }
return cart;

    }


}
