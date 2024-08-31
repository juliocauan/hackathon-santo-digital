package br.julio.mariano.hackathon_santo_digital.infrastructure.repository.specification;

import java.util.ArrayList;
import java.util.List;

import org.openapitools.model.OrderEnum;
import org.openapitools.model.ProductFilter;
import org.springframework.data.jpa.domain.Specification;

import br.julio.mariano.hackathon_santo_digital.domain.model.Product;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;

public interface ProductSpecification {

    static Specification<Product> filter(ProductFilter filter) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            List<Order> orders = new ArrayList<>();

            if (filter.getProductNumber() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("productNumber"), filter.getProductNumber()));
            }

            if (filter.getColor() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(root.get("color"), filter.getColor()));
            }

            if (filter.getName() != null) {
                orders.add(filter.getName() == OrderEnum.ASC ? criteriaBuilder.asc(root.get("name"))
                        : criteriaBuilder.desc(root.get("name")));
            }

            if (filter.getStandardCost() != null) {
                orders.add(filter.getStandardCost() == OrderEnum.ASC ? criteriaBuilder.asc(root.get("standardCost"))
                        : criteriaBuilder.desc(root.get("standardCost")));
            }

            criteriaQuery.orderBy(orders);
            return predicate;
        };
    }

}
