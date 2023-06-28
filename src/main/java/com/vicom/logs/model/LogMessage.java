package com.vicom.logs.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LogMessage implements Serializable {

	private static final long serialVersionUID = 586077130713613183L;
	
	private String timestamp;
	private String source;
	private String severity;
	private String message;
	private String component;
	private String hostname;

}
