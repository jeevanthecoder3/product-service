package com.greenry.productservice.Controllers;

import com.greenry.productservice.Dao.*;
import com.greenry.productservice.Entities.*;
import com.greenry.productservice.PayLoad.FileResponse;
import com.greenry.productservice.Service.FileService;
import com.greenry.productservice.Service.ProductService;
import com.greenry.productservice.Service.UserService;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        if((sellerName==null || product==null) || userService.checkValidity(sellerName)==false){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }else{
            User user = this.userRepository.findUserByUserName(sellerName);
            Product product2 = this.productRepository.findProductByPTitle(ptitle);
            Cart cart = user.getCart();
            int price = product2.getPPrice();
            Set<ItemQuantity> itemQuantitySet = cart.getItemQuantity();
            for(ItemQuantity itemQuantity:itemQuantitySet){
                if(itemQuantity.getProduct().getPTitle().equals(ptitle))
                    itemQuantity.setProduct(null);
            }
            ResponseEntity<Product> response=deleteProduct(ptitle,sellerName);

            ResponseEntity<Product> product1 = uploadProduct(sellerName,product);
            for(ItemQuantity quantity:itemQuantitySet) {
                if (quantity.getProduct() == null) {
                    quantity.setProduct(product);
                    if(quantity.getQuantity()==product.getQuantity() && price!=product.getPPrice()){
                        cart.setTotalCost(cart.getTotalCost()-
                                (quantity.getQuantity()*price)+(product.getQuantity()*product.getPPrice()));

                    }
                    if(quantity.getQuantity()>product.getQuantity() && price!=product.getPPrice()){
                        System.out.println();
                        cart.setTotalCost(cart.getTotalCost()-
                    (quantity.getQuantity()*price)+(product.getQuantity()*product.getPPrice()));
                        quantity.setQuantity(product.getQuantity());
                        this.cartRepository.save(cart);
                    }
                    if(quantity.getQuantity()<product.getQuantity() && price!=product.getPPrice()){
                        cart.setTotalCost(cart.getTotalCost()-
                                (quantity.getQuantity()*price)+(quantity.getQuantity()*product.getPPrice()));
                        this.cartRepository.save(cart);
                    }

                    if(quantity.getQuantity()>product.getQuantity() && price==product.getPPrice()){
                        cart.setTotalCost(cart.getTotalCost()-((quantity.getQuantity()-product.getQuantity()) * product.getPPrice()));
                        quantity.setQuantity(product.getQuantity());
                        this.cartRepository.save(cart);
                    }
                    this.itemQuantityRepository.save(quantity);
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
            if (itemQuantity != null) {
                itemQuantityRepository.delete(itemQuantity);
                Cart cart = itemQuantity.getCart();
                cart.setTotalCost(cart.getTotalCost()-(itemQuantity.getQuantity()*itemQuantity.getProduct().getPPrice()));
                cart.getItemQuantity().remove(itemQuantity);
                cartRepository.save(cart);

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
