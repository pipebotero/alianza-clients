package tech.alianza.clients.advsearch;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import tech.alianza.clients.domain.Client;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

public class ClientSpecification implements Specification<Client> {

    private final SearchCriteria searchCriteria;

    public ClientSpecification(final SearchCriteria searchCriteria){
        super();
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Specification<Client> and(Specification<Client> other) {
        return Specification.super.and(other);
    }

    @Override
    public Specification<Client> or(Specification<Client> other) {
        return Specification.super.or(other);
    }

    @Override
    public Predicate toPredicate(Root<Client> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        String strToSearch = searchCriteria.getValue().toString().toLowerCase();

        switch(Objects.requireNonNull(SearchOperation.getSimpleOperation(searchCriteria.getOperation()))){
            case CONTAINS:
                return criteriaBuilder.like(criteriaBuilder.lower(root.get(searchCriteria.getFilterKey())), "%" + strToSearch + "%");

            case DOES_NOT_CONTAIN:
                return criteriaBuilder.notLike(criteriaBuilder.lower(root.get(searchCriteria.getFilterKey())), "%" + strToSearch + "%");

            case BEGINS_WITH:
                return criteriaBuilder.like(criteriaBuilder.lower(root.get(searchCriteria.getFilterKey())), strToSearch + "%");

            case DOES_NOT_BEGIN_WITH:
                return criteriaBuilder.notLike(criteriaBuilder.lower(root.get(searchCriteria.getFilterKey())), strToSearch + "%");

            case ENDS_WITH:
                return criteriaBuilder.like(criteriaBuilder.lower(root.get(searchCriteria.getFilterKey())), "%" + strToSearch);

            case DOES_NOT_END_WITH:
                return criteriaBuilder.notLike(criteriaBuilder.lower(root.get(searchCriteria.getFilterKey())), "%" + strToSearch);

            case EQUAL:
                return criteriaBuilder.equal(root.get(searchCriteria.getFilterKey()), searchCriteria.getValue());

            case NOT_EQUAL:
                return criteriaBuilder.notEqual(root.get(searchCriteria.getFilterKey()), searchCriteria.getValue());

            case NUL:
                return criteriaBuilder.isNull(root.get(searchCriteria.getFilterKey()));

            case NOT_NULL:
                return criteriaBuilder.isNotNull(root.get(searchCriteria.getFilterKey()));

            case GREATER_THAN:
                if(searchCriteria.getFilterKey().equals("createdAt")){
                    String date = searchCriteria.getValue().toString();
                    LocalDate localDate = LocalDate.parse(date);
                    return criteriaBuilder.greaterThan(root.<LocalDate> get(searchCriteria.getFilterKey()), localDate);
                }
                return criteriaBuilder.greaterThan(root.<String> get(searchCriteria.getFilterKey()), searchCriteria.getValue().toString());

            case GREATER_THAN_EQUAL:
                if(searchCriteria.getFilterKey().equals("createdAt")){
                    String date = searchCriteria.getValue().toString();
                    LocalDate localDate = LocalDate.parse(date);
                    return criteriaBuilder.greaterThanOrEqualTo(root.<LocalDate> get(searchCriteria.getFilterKey()), localDate);
                }
                return criteriaBuilder.greaterThanOrEqualTo(root.<String> get(searchCriteria.getFilterKey()), searchCriteria.getValue().toString());

            case LESS_THAN:
                if(searchCriteria.getFilterKey().equals("createdAt")){
                    String date = searchCriteria.getValue().toString();
                    LocalDate localDate = LocalDate.parse(date);
                    return criteriaBuilder.lessThan(root.<LocalDate> get(searchCriteria.getFilterKey()), localDate);
                }
                return criteriaBuilder.lessThan(root.<String> get(searchCriteria.getFilterKey()), searchCriteria.getValue().toString());

            case LESS_THAN_EQUAL:
                if(searchCriteria.getFilterKey().equals("createdAt")){
                    String date = searchCriteria.getValue().toString();
                    LocalDate localDate = LocalDate.parse(date);
                    return criteriaBuilder.lessThanOrEqualTo(root.<LocalDate> get(searchCriteria.getFilterKey()), localDate);
                }
                return criteriaBuilder.lessThanOrEqualTo(root.<String> get(searchCriteria.getFilterKey()), searchCriteria.getValue().toString());
        }
        return null;
    }


}
