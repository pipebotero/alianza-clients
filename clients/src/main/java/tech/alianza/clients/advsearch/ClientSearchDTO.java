package tech.alianza.clients.advsearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientSearchDTO {
    private List<SearchCriteria> searchCriteriaList;
    private String dataOption;
}
