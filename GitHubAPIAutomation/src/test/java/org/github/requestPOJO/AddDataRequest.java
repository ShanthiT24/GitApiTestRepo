package org.github.requestPOJO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
/*{"name":"Hello-World","description":"This is your first
repo!","homepage":"https://github.com","private":false,"}*/

public class AddDataRequest {
    @JsonProperty(value = "name")
    public String name;
    @JsonProperty(value = "description")
    public String description;
    @JsonProperty(value = "homepage")
    public String homepage;
   // @JsonProperty(value = "private")
    //public String privateval;
}
