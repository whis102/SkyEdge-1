package SkyEdge.controller;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import SkyEdge.model.Product;
import SkyEdge.model.ProductDto;
import SkyEdge.model.User;
import SkyEdge.repository.ProductRepository;
import SkyEdge.service.ProductService;
import jakarta.validation.Valid;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/admin/product")
    public String listProducts(Model model) {

        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);

        Long productCount = productService.getProductCount();
        model.addAttribute("productCount", productCount);

        return "admin/product/admin-product";
    }

    @GetMapping("/admin/product/add")
    public String addProduct(Model model) {
        ProductDto productDto = new ProductDto();
        model.addAttribute("productDto", productDto);
        return "admin/product/admin-addproduct";
    }

    @PostMapping("/admin/product/add")
    public String addNewProduct(@Valid @ModelAttribute ProductDto productDto, BindingResult result,
            @AuthenticationPrincipal User user) {

        MultipartFile image = productDto.getImageFile();
        if (image == null || image.isEmpty()) {
            result.rejectValue("imageFile", "NotEmpty", "Image is required");
            return "admin/product/admin-addproduct";
        }

        if (result.hasErrors()) {
            return "admin/product/admin-addproduct";
        }
        Date CreateAt = new Date();
        String storageFileName = CreateAt.getTime() + "_" + image.getOriginalFilename();
        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setImageFileName(storageFileName);
        product.setCountry(productDto.getCountry());
        product.setManufacturer(productDto.getManufacturer());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());
        product.setCategory(productDto.getCategory());
        product.setCountry(productDto.getCountry());
        product.setDiscount(productDto.getDiscount());
        product.setFront(productDto.getFront());
        product.setSide(productDto.getSide());
        product.setWidth(productDto.getWidth());
        product.setHeight(productDto.getHeight());
        product.setCreatedBy(user);
        productRepository.save(product);
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/search")
    public String searchProduct(@RequestParam("query") String query, Model model) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(query);
        System.out.println(products);
        model.addAttribute("products", products);
        Long productCount = productService.getProductCount();
        model.addAttribute("productCount", productCount);
        return "admin/product/admin-product";
    }

    @GetMapping("/admin/product/delete")
    public String deleteProduct(@RequestParam int id) {
        if (productRepository.findById(id).isPresent()) {
            Product product = productRepository.findById(id).get();
            productRepository.delete(product);
        }
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/edit")
    public String showUpdateProduct(Model model, @RequestParam int id) {
        try {
            Product product = productRepository.findById(id).get();
            model.addAttribute("product", product);

            ProductDto productDto = new ProductDto();
            productDto.setName(product.getName());
            productDto.setDescription(product.getDescription());
            productDto.setCountry(product.getCountry());
            productDto.setManufacturer(product.getManufacturer());
            productDto.setPrice(product.getPrice());
            productDto.setStock(product.getStock());
            productDto.setCategory(product.getCategory());
            productDto.setCountry(product.getCountry());
            productDto.setDiscount(product.getDiscount());
            productDto.setFront(product.getFront());
            productDto.setSide(product.getSide());
            productDto.setWidth(product.getWidth());
            productDto.setHeight(product.getHeight());
            model.addAttribute("productDto", productDto);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/admin/product";
        }
        return "admin/product/admin-updateproduct";
    }

    @PostMapping("/admin/product/edit")
    public String updateProduct(Model model, @RequestParam int id, @Valid @ModelAttribute ProductDto productDto,
            BindingResult result) {
        try {
            Product product = productRepository.findById(id).get();
            model.addAttribute("product", product);

            // if (result.hasErrors()){
            // return "admin/product/admin-updateproduct";
            // }
            if (!productDto.getImageFile().isEmpty()) {
                // delete old image
                String uploadDir = "public/images/";
                Path oldImagePath = Paths.get(uploadDir + product.getImageFileName());

                try {
                    Files.delete(oldImagePath);
                } catch (Exception ex) {
                    System.out.println("Exception: " + ex.getMessage());
                }

                // Save new image file

                MultipartFile image = productDto.getImageFile();
                Date CreateAt = new Date();
                String storageFileName = CreateAt.getTime() + "_" + image.getOriginalFilename();

                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                            StandardCopyOption.REPLACE_EXISTING);
                }
                product.setImageFileName(storageFileName);
            }
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setCountry(productDto.getCountry());
            product.setManufacturer(productDto.getManufacturer());
            product.setPrice(productDto.getPrice());
            product.setStock(productDto.getStock());
            product.setCategory(productDto.getCategory());
            product.setCountry(productDto.getCountry());
            product.setDiscount(productDto.getDiscount());
            product.setFront(productDto.getFront());
            product.setSide(productDto.getSide());
            product.setWidth(productDto.getWidth());
            product.setHeight(productDto.getHeight());
            productRepository.save(product);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        return "redirect:/admin/product";
    }
}
