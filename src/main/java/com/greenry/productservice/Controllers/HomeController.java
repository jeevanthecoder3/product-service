package com.greenry.productservice.Controllers;

import com.greenry.productservice.Dao.*;
import com.greenry.productservice.Entities.*;
import com.greenry.productservice.Service.FileService;
import com.greenry.productservice.Service.ProductService;
import com.greenry.productservice.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

@RestController
@RequestMapping("/seller/{sellerName}")
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FileService fileService;


    @Autowired
    private ItemQuantityRepository itemQuantityRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FileController fileController;

    @PostMapping("/upload-product")
    public ResponseEntity<Product> uploadProduct(@PathVariable("sellerName")String sellerName, @RequestBody Product product){
        if((sellerName==null || product==null) || userService.checkValidity(sellerName)==false){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }else{
            Product product1=productService.uploadProduct(sellerName,product);
            return ResponseEntity.ok(product1);
        }

    }

    @PostMapping("/update-product/{pTitle}")
    public ResponseEntity<Product> updateProduct(@PathVariable("sellerName")String sellerName,@PathVariable("pTitle")String ptitle,
                                                 @RequestBody Product product) throws IOException {

        int flag=0;
        if((sellerName==null || product==null) || userService.checkValidity(sellerName)==false){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }else{
            ItemQuantity itemQuantity1=null;
            User user = this.userRepository.findUserByUserName(sellerName);
            Product product2 = this.productRepository.findProductByPTitle(ptitle);
            if(product2.getItemQuantity()!=null)
            itemQuantity1 = product2.getItemQuantity();
            int q=product2.getItemQuantity().getQuantity();
            Cart cart = user.getCart();
            int totalcost = 0;
            int price = product2.getPPrice();
            Set<ItemQuantity> itemQuantitySet = cart.getItemQuantity();
            if(itemQuantitySet!=null)
            for(ItemQuantity itemQuantity:itemQuantitySet){
                if(itemQuantity!=null && itemQuantity.getProduct()!=null && itemQuantity.getProduct().getPTitle().equals(ptitle)) {
                    flag=1;
                    totalcost=itemQuantity.getQuantity()*itemQuantity.getProduct().getPPrice();
                    cart.getItemQuantity().remove(itemQuantity);
                    cartRepository.save(cart);
                    break;
                }
            }
            ResponseEntity<Product> response=deleteProduct(ptitle,sellerName);


            ResponseEntity<Product> product1 = uploadProduct(sellerName,product);
            if(itemQuantity1!=null) {
                ItemQuantity i1 = new ItemQuantity();
                i1.setProduct(product1.getBody());
                i1.setCart(cart);
                i1.setQuantity(q);
                product.setItemQuantity(i1);
                i1.setCart(cart);
                cart.setTotalCost(cart.getTotalCost()+totalcost);
                cart.getItemQuantity().add(i1);
                itemQuantityRepository.save(i1);
                productRepository.save(product);
            }

            Set<ItemQuantity> itemQuantitySet1 = cart.getItemQuantity();
            for(ItemQuantity quantity:itemQuantitySet1) {
                 {
                    System.out.println(quantity);
                    quantity.setProduct(product);

                     this.itemQuantityRepository.save(quantity);
                    if(quantity.getQuantity()==product.getQuantity() && price!=product.getPPrice()){
                        System.out.println("case 1");

                        int cost=cart.getTotalCost()-
                                (quantity.getQuantity()*price)+(product.getQuantity()*product.getPPrice());
                        if(cost>0)
                        cart.setTotalCost(cost);
                        else {
                            cart.setTotalCost(0);
                        }

                        this.cartRepository.save(cart);
                        break;
                    }
                    if(quantity.getQuantity()==product.getQuantity() && price==product.getPPrice()){
                        System.out.println("case 2");
                        int cost=cart.getTotalCost()-
                                (quantity.getQuantity()*price)+(product.getQuantity()*product.getPPrice());
                        if(cost>0)
                            cart.setTotalCost(cost);
                        else
                            cart.setTotalCost(0);

                        this.cartRepository.save(cart);
                        break;

                    }
                    if(quantity.getQuantity()>product.getQuantity() && price!=product.getPPrice()){
                        System.out.println("case 3");
                        System.out.println();
                        int cost=cart.getTotalCost()-
                                (quantity.getQuantity()*price)+(product.getQuantity()*product.getPPrice());
                        if(cost>0)
                        cart.setTotalCost(cost);
                        else
                            cart.setTotalCost(0);
                        quantity.setQuantity(product.getQuantity());

                        this.itemQuantityRepository.save(quantity);
                        this.cartRepository.save(cart);
                        break;
                    }
                    if(quantity.getQuantity()<product.getQuantity() && price!=product.getPPrice()){
                        System.out.println("case 4");
                        int cost=cart.getTotalCost()-
                                (quantity.getQuantity()*price)+(quantity.getQuantity()*product.getPPrice());
                        System.out.println(cart.getTotalCost()+" "+(quantity.getQuantity()*price)+" "+(quantity.getQuantity()*product.getPPrice()));
                        if(cost>0)
                        cart.setTotalCost(cost);
                        else
                            cart.setTotalCost(0);
                        this.cartRepository.save(cart);
                        break;
                    }
                    if(quantity.getQuantity()<product.getQuantity() && price==product.getPPrice()){
                        System.out.println("case 5");

                        int cost=cart.getTotalCost()-
                                (quantity.getQuantity()*price)+(quantity.getQuantity()*product.getPPrice());
                        System.out.println(cart.getTotalCost()+" "+
                                (quantity.getQuantity()*price)+" "+(quantity.getQuantity()*product.getPPrice()));
                        if(cost>0)
                            cart.setTotalCost(cost);
                        else if((quantity.getQuantity()*price) == (quantity.getQuantity()*product.getPPrice()) && cart.getTotalCost()==0)
                            cart.setTotalCost((quantity.getQuantity()*product.getPPrice()));
                        else
                            cart.setTotalCost(0);
                        this.cartRepository.save(cart);
                        break;
                    }
                    if(quantity.getQuantity()>product.getQuantity() && price==product.getPPrice()){
                        System.out.println("case 6");
                        int cost=cart.getTotalCost()-((quantity.getQuantity()-product.getQuantity()) * product.getPPrice());
                        System.out.println(cart.getTotalCost()+" "+((quantity.getQuantity()-product.getQuantity()) * product.getPPrice()));
                        if(cost>0)
                        cart.setTotalCost(cost);
                        else cart.setTotalCost(0);
                        quantity.setQuantity(product.getQuantity());
                        this.cartRepository.save(cart);

                        this.itemQuantityRepository.save(quantity);
                        break;
                    }
                }
            }
            return ResponseEntity.ok(product1.getBody());
        }

    }



    @DeleteMapping("/delete-product/{ptitle}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("ptitle") String ptitle,@PathVariable("sellerName")String sellerName){
        if((sellerName==null || ptitle==null) || userService.checkValidity(sellerName)==false){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }else{
            Product product = productRepository.findProductByPTitle(ptitle);
            Seller seller = sellerRepository.findSellerBySellerName(sellerName);
            seller.getProduct().remove(product);
            try {
                if (product.getImage() != null)
                    Files.delete(Path.of("images/" + product.getImage()));
            }catch(Exception e){
                System.out.println(e.toString());
            }

            ItemQuantity itemQuantity = product.getItemQuantity();

            product.setItemQuantity(null);
//            itemQuantity.setProduct(null);
//            itemQuantityRepository.delete(itemQuantity);
            if (itemQuantity != null) {
                System.out.println("in here");
                System.out.println(itemQuantity.getQid());
                Cart cart = itemQuantity.getCart();
                cart.setTotalCost(cart.getTotalCost()-(itemQuantity.getQuantity()*itemQuantity.getProduct().getPPrice()));
                cart.getItemQuantity().remove(itemQuantity);
                cartRepository.save(cart);

                itemQuantityRepository.delete(itemQuantity);

            }
            productRepository.delete(product);

            return ResponseEntity.ok(product);
        }


    }



    @GetMapping("/get-products")
    public ResponseEntity<Set<Product>> getProductsofSeller(@PathVariable("sellerName")String sellerName){
        Seller seller = this.sellerRepository.findSellerBySellerName(sellerName);
        if(seller==null)
            return ResponseEntity.ok(this.productRepository.getProducts());
        if(seller!=null && userService.checkValidity(sellerName)==true)
        return ResponseEntity.ok(seller.getProduct());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/product/{ptitle}")
    public ResponseEntity<Product> getProduct(@PathVariable("ptitle")String ptitle){
        Product product = this.productRepository.findProductByPTitle(ptitle);
        if(product==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else
            return ResponseEntity.ok(product);

    }
}
