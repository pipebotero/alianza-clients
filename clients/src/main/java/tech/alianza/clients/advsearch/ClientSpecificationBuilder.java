package tech.alianza.clients.advsearch;

import org.springframework.data.jpa.domain.Specification;
import tech.alianza.clients.domain.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientSpecificationBuilder {
    private final List<SearchCriteria> params;

    public ClientSpecificationBuilder(){
        this.params = new ArrayList<>();
    }

    public final ClientSpecificationBuilder with(String key, String operation, Object value){
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public final ClientSpecificationBuilder with(SearchCriteria searchCriteria){
        params.add(searchCriteria);
        return this;
    }

    public Specification<Client> build(){
        if(params.size() == 0){
            return null;
        }

        Specification<Client> result = new ClientSpecification(params.get(0));
        for (int idx = 1; idx < params.size(); idx++){
            SearchCriteria criteria = params.get(idx);
            result = SearchOperation.getDataOption(criteria.getDataOption()) == SearchOperation.ALL
                    ? Specification.where(result).and(new ClientSpecification(criteria))
                    : Specification.where(result).or(new ClientSpecification(criteria));
        }

        return result;
    }
}
