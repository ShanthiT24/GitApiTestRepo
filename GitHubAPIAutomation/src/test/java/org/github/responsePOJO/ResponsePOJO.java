
package org.github.responsePOJO;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ResponsePOJO {
	//public ownerPojo owner1 = null; //new ownerPojo();
    //ResponsePOJO.owner = owner1;
    @JsonProperty(value = "status")
    public String status;
    
    // Get single repo info
    @JsonProperty(value = "full_name")
    public String fullName;
    @JsonProperty(value = "default_branch")
    public String defaultBranch;
    
    //get error msg for non existent repo req
    @JsonProperty(value = "message")
    public String message ;
    
    //Get all repo info
    @JsonProperty(value = "private")
    public String Private ;
    @JsonProperty(value = "visibility")
    public String visibility ;
    
    //create repo
    @JsonProperty(value = "name")
    public String name ;
    @JsonProperty(value = "owner")
    public OwnerPojo owner ;
    
    //create repo with existing repo name
    @JsonProperty(value = "errors")
    public List<ErrorsPojo> errors ;
     
}



