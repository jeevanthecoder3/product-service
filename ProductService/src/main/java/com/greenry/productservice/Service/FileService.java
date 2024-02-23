package com.greenry.productservice.Service;

import com.greenry.productservice.Dao.FileRepository;
import com.greenry.productservice.Dao.ProductRepository;
import com.greenry.productservice.Dao.SellerRepository;
import com.greenry.productservice.Entities.Product;
import com.greenry.productservice.Entities.Seller;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Service
public class FileService implements FileRepository {
    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private ProductRepository productRepository;
    @Override
    @Transactional
    public String uploadImage(String path, MultipartFile file,String userName,String pTitle) throws IOException {

        //File name
        String name = file.getOriginalFilename();


        String randomID = UUID.randomUUID().toString();
        String fileName1=randomID.concat(name.substring(name.lastIndexOf(".")));

        //Fullpath
        String filePath=path+ File.separator + fileName1;

        //Create folder if not created
        File file1=new File(path);

        if(!file1.exists()){
            file1.mkdir();
        }

        //file copy
        Files.copy(file.getInputStream(), Paths.get(filePath));

        //updating the database
        Seller seller = sellerRepository.findSellerBySellerName(userName);
        if(seller!=null){
            Product product = productRepository.findProductByPTitle(pTitle);
            if(product!=null) {
                product.setPImageURL("http://localhost:8002/" + userName + "/file/images/" + fileName1);
                product.setImage(fileName1);
                productRepository.save(product);
            }else
                throw new RuntimeException("Product Not Found!!!");

        }else
            throw new RuntimeException("Seller Not Found !!!!");

        return name;

    }

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
        String fullPath = path+File.separator+fileName;
        InputStream is = new FileInputStream(fullPath);
        return is;
    }
}
