package com.greenry.productservice.Controllers;

import com.greenry.productservice.Dao.ProductRepository;
import com.greenry.productservice.Dao.SellerRepository;
import com.greenry.productservice.Entities.Product;
import com.greenry.productservice.Entities.Seller;
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
    private SellerRepository sellerRepository;

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
            Product product1=productService.updateProduct(sellerName,product,ptitle);
            return ResponseEntity.ok(product1);
        }

    }



    @DeleteMapping("/delete-product/{ptitle}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("ptitle") String ptitle,@PathVariable("sellerName")String sellerName) throws IOException {
        if((sellerName==null || ptitle==null) || userService.checkValidity(sellerName)==false){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }else{
            Product product = productRepository.findProductByPTitle(ptitle);
            Seller seller = sellerRepository.findSellerBySellerName(sellerName);
            seller.getProduct().remove(product);
            Files.delete(Path.of("images/" + product.getImage()));
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
