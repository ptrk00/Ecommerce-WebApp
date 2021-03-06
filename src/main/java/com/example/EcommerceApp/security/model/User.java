package com.example.EcommerceApp.security.model;

import com.example.EcommerceApp.order.ProductOrder;
import com.example.EcommerceApp.product.model.Product;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    @Size(min = 5)
    private String username;

    @Size(min = 5)
    private String password;

    @Column(unique = true)
    @Email
    private String email;

    private Boolean enabled;

    // TODO: fix EAGER fetch type

    @ToString.Exclude
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<ProductOrder> productOrders;

    @OneToMany(mappedBy = "seller", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Product> offers;

    public void addOffer(Product product) {offers.add(product);}

    public void addOrder(ProductOrder order) {
        productOrders.add(order);
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.enabled = false;
        productOrders = new LinkedList<>();
        offers = new LinkedList<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    // TODO: implement

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
