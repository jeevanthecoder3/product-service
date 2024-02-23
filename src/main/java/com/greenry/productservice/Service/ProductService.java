package com.greenry.productservice.Service;

import com.greenry.productservice.Dao.ProductRepository;
import com.greenry.productservice.Dao.SellerRepository;
import com.greenry.productservice.Entities.Product;
import com.greenry.productservice.Entities.Seller;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

@Service
public class ProductService {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private ProductRepository productRepository;


    @Value("${project.image}")
    private String path;

    @Transactional
    public Product uploadProduct(String sellerName, Product product){
        Seller seller = sellerRepository.findSellerBySellerName(sellerName);
        if(seller!=null){
            Set<Product> product1 = seller.getProduct();
            if(product1==null){
                Set<Product> pset = new HashSet<>();
                pset.add(product);
                seller.setProduct(pset);
            }else{
                seller.getProduct().add(product);
            }
            product.setSeller(seller);
            seller.setNoOfProducts(seller.getNoOfProducts()+1);
            return productRepository.save(product);
        }else{
            throw new RuntimeException("Seller Not Found!!!");
        }
    }




    @Transactional
    public Product updateProduct(String sellerName,Product product,String ptitle) throws IOException {
        Seller seller = sellerRepository.findSellerBySellerName(sellerName);
        Product product1=productRepository.findProductByPTitle(ptitle);
        if(seller!=null){

                String imageurl= product1.getPImageURL();
                String image=product1.getImage();
                seller.getProduct().remove(product1);
                productRepository.delete(product1);
                Product p = uploadProduct(sellerName,product);
                p.setPImageURL(imageurl);
                p.setImage(image);
                return productRepository.save(p);


        }else{
            throw new RuntimeException("Seller not found!!!");
        }
    }
}
