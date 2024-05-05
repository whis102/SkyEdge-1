package SkyEdge.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    // @Column(name = "price", precision = 10, scale = 2)
    // private BigDecimal price;

    // @Column(name = "stock")
    // private int stock;


    // @Column(name = "country", length = 100)
    // private String country;

    // @Column(name = "manufacturer", length = 100)
    // private String manufacturer;

    // @Column(name = "category", length = 50)
    // private String category;

    // @Column(name = "discount", length = 10)
    // private String discount;

    // @Column(name = "front", length = 10)
    // private String front;

    // @Column(name = "side", length = 10)
    // private String side;

    // @Column(name = "width", length = 10)
    // private String width;

    // @Column(name = "height", length = 10)
    // private String height;

    // public Product(String productName, BigDecimal price, int stock, String
    // description, String country,
    // String manufacturer, String category, String discount, String front, String
    // side, String width,
    // String height) {
    // this.productName = productName;
    // this.price = price;
    // this.stock = stock;
    // this.description = description;
    // this.country = country;
    // this.manufacturer = manufacturer;
    // this.category = category;
    // this.discount = discount;
    // this.front = front;
    // this.side = side;
    // this.width = width;
    // this.height = height;
    // }

}
