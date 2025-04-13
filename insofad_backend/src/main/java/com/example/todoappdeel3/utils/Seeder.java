package com.example.todoappdeel3.utils;

import com.example.todoappdeel3.dao.*;
import com.example.todoappdeel3.models.*;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Seeder {
    private final ProductDAO productDAO;
    private final UserRepository userRepository;
    private final CategoryDAO categoryDAO;
    private final OrderRepository orderRepository;
    private final OptionsRepository optionsRepository;
    private final ProductVariantRepository productVariantRepository;

    public Seeder(ProductDAO productDAO, UserRepository userRepository, CategoryDAO categoryDAO, OrderRepository orderRepository, ProductVariantRepository productVariantRepository, OptionsRepository optionsRepository) {
        this.productDAO = productDAO;
        this.userRepository = userRepository;
        this.categoryDAO = categoryDAO;
        this.orderRepository = orderRepository;
        this.productVariantRepository = productVariantRepository;
        this.optionsRepository = optionsRepository;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event){
        this.seedProducts();
        this.seedAdmin();
    }

    private void seedProducts(){
        // Luxe categorieën aanmaken
        Category categoryHoodies = new Category("Luxury Hoodies");
        Category categoryPants = new Category("Designer Pants");
        Category categoryAccessories = new Category("Exclusive Accessories");
        Category categoryCapsAndBeanies = new Category("Designer Headwear");
        Category categoryOuterwear = new Category("Premium Outerwear");

        // Luxe producten definiëren
        Product[] products = {

                new Product("Cashmere Hoodie", "Crafted from 100% pure cashmere, this hoodie offers unparalleled softness and warmth, ideal for those who seek both comfort and luxury.", 400.00, categoryHoodies, "A+", "Tailored Fit", "https://image-resizing.booztcdn.com/filippa-k/fk28929_cgreymelan_v1448.webp?has_grey=1&has_webp=1&size=source\n", 5),
                new Product("Silk Hoodie", "Experience the smooth touch of pure silk with this hoodie, combining casual design with luxury fabric for a sophisticated, relaxed look.", 350.00, categoryHoodies, "A++", "Tailored Fit", "https://www.globalblank.com/cdn/shop/products/PRM4500TD-AQUABLUE-1_590x.png?v=1611605760\n", 7),
                new Product("White Wool Hoodie", "Made from fine wool, this white hoodie blends comfort with elegance, perfect for a subtle, stylish statement.", 320.00, categoryHoodies, "A++",  "Tailored Fit", "https://statb.jrmstatic.com/gran-sasso-cardigan-wool-hoodie-vest-off-white-iz22a6nu7voplwa9.jpg\n", 8),
                new Product("Italian Leather Pants", "Tailored from premium Italian leather, these pants offer a perfect blend of luxury and durability, tailored for the fashion-forward.", 700.00, categoryPants, "A+", "Slim Fit", "https://www.alyxstudio.com/cdn/shop/files/AAWPA0413LE01_BLK0001_F23_2_1024x.png?v=1695285925\n", 1),
                new Product("Suede Cargo Pants", "Elevate your style with these soft suede cargo pants, offering both comfort and a bold fashion statement.", 650.00, categoryPants, "A+", "Relaxed Fit", "https://designmenswear.com/cdn/shop/products/OliveCargoElasticBottomJagger2PocketsWithZipperSLIM-FITMadeInTurkey_720x.png?v=1666415226\n", 3),
                new Product("Velvet Parachute Pants", "These parachute pants crafted from premium velvet redefine luxury in streetwear with their unique texture and comfort.", 600.00, categoryPants, "A++", "Over-sized", "https://s3-eu-west-1.amazonaws.com/images.linnlive.com/1c395707d48f63fc98d61cde51cda969/2dadf51c-0cce-4ab0-bfc9-c9f151ff79be.jpg\n", 7),
                new Product("Leather Satchel Bag", "Handcrafted from the finest leather, this satchel is a masterpiece of luxury accessories, perfect for the discerning individual.", 950.00, categoryAccessories, "A+", "30cm x 35cm", "https://cdn.webshopapp.com/shops/270371/files/345410622/1600x2048x2/hillburry-leather-bags-hillburry-unisex-leather-sh.jpg\n", 1),
                new Product("Exotic Skin Clutch Bag", "This clutch made from exotic animal skin stands out as a symbol of luxury and exclusivity, perfect for high-class events.", 1100.00, categoryAccessories, "A+", "25cm x 15cm", "https://cdn11.bigcommerce.com/s-mim05/images/stencil/1280x1280/products/1585/5867/G1-067-2__11422.1425428430.jpg?c=2\n", 1),
                new Product("Gold-Plated Keychain", "A stunning gold-plated keychain that combines utility with opulence, making it an essential luxury for everyday elegance.", 200.00, categoryAccessories, "A+", "8cm", "https://i.etsystatic.com/7744338/r/il/f5e3e7/544844798/il_fullxfull.544844798_fp59.jpg\n", 7),
                new Product("Merino Wool Beanie", "Crafted from 100% merino wool, this beanie provides exceptional warmth and style, making it a must-have accessory for any luxury wardrobe.", 120.00, categoryCapsAndBeanies, "A++", "Fitted", "https://www.rustek.co/cdn/shop/products/doubleribmerinobeanietan-570027.png?v=1699814689&width=823\n", 2),
                new Product("Cashmere Flat Cap", "Made from soft cashmere, this flat cap offers a timeless look with luxurious comfort, perfect for stylish outdoor adventures.", 150.00, categoryCapsAndBeanies, "A++", "Fitted", "https://cdn.shoplightspeed.com/shops/632556/files/47966488/750x2000x3/classic-eb-191-wool-cashmere-cap-city-sport.jpg\n", 5),
                new Product("Silk Bucket Hat", "This silk bucket hat combines casual style with a touch of luxury, ideal for those who appreciate fine fabrics and contemporary design.", 180.00, categoryCapsAndBeanies, "A++", "Fitted", "https://sissel-edelbo.dk/cdn/shop/files/SE642.1-237_1000x.webp?v=1715326855\n", 8)
        };

        // Producten opslaan
        for (Product product : products) {
            this.productDAO.createProduct(product);
        }

        // Categorieën opslaan
        this.categoryDAO.createCategory(categoryHoodies);
        this.categoryDAO.createCategory(categoryPants);
        this.categoryDAO.createCategory(categoryAccessories);
        this.categoryDAO.createCategory(categoryCapsAndBeanies);
        this.categoryDAO.createCategory(categoryOuterwear);

        // Varianten en opties definiëren
        String[] variantNames = {"Grootte", "Kleur", "Print"};
        String[][] options = {
                {"S", "M", "L"},
                {"Red", "Blue", "Black"},
                {"Grote Print", "Medium Print", "Small Print"}
        };

        for (Product product : products) {
            for (int i = 0; i < variantNames.length; i++) {
                ProductVariant variant = new ProductVariant(variantNames[i], "De " + variantNames[i].toLowerCase() + " van de " + product.getName(), product);
                this.productVariantRepository.save(variant);

                for (String optionName : options[i]) {
                    Options option = new Options(optionName, 100 + i * 50, variant);
                    this.optionsRepository.save(option);
                }
            }
        }
    }

    private void seedAdmin(){
        String encodedPassword = new BCryptPasswordEncoder().encode("!nsadd3twW68Ez");
        CustomUser customUser = new CustomUser("bobluxuryproducts@mail.com", encodedPassword, "ROLE_ADMIN"); // ROLE_ IS A MUST FOR SPRING SECURITY
        userRepository.save(customUser);
    }
}
