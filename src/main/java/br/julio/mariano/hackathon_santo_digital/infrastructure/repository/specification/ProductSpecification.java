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

            if (filter == null)
                return predicate;

            if (filter.getNumber() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("number"), filter.getNumber()));
            }

            if (filter.getColour() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(root.get("colour"), filter.getColour().getValue()));
            }

            if (filter.getName() != null) {
                orders.add(filter.getName() == OrderEnum.ASC ? criteriaBuilder.asc(root.get("name"))
                        : criteriaBuilder.desc(root.get("name")));
            }

            if (filter.getPrice() != null) {
                orders.add(filter.getPrice() == OrderEnum.ASC ? criteriaBuilder.asc(root.get("price"))
                        : criteriaBuilder.desc(root.get("price")));
            }

            criteriaQuery.orderBy(orders);
            return predicate;
        };
    }

}
