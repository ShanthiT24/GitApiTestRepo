package org.github.responsePOJO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class ErrorsPojo 
{
		@JsonProperty(value = "resource")
	    public String resource ;
		@JsonProperty(value = "code") 
		public String code ;
		@JsonProperty(value = "field") 
		public String field ;
		@JsonProperty(value = "message") 
		public String msg ;
}



