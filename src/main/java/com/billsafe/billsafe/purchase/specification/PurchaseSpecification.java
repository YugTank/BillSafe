package com.billsafe.billsafe.purchase.specification;

import com.billsafe.billsafe.auth.entity.User;
import com.billsafe.billsafe.purchase.entity.Purchase;
import org.springframework.data.jpa.domain.Specification;

public class PurchaseSpecification {

    public static Specification<Purchase> hasCategory(String category){

        if(category==null || category.isBlank()){
            return null;
        }

        return (root, query, cb)->
                cb.equal(root.get("category"), category);
    }

    public static Specification<Purchase> hasBrand(String brand){

        if(brand==null || brand.isBlank()){
            return null;
        }

        return (root, query, cb)->
                cb.equal(root.get("brand"), brand);
    }

    public static Specification<Purchase> belongsToUser(User user){
        return (root, query, cb)->
                cb.equal(root.get("user"), user);
    }
}
