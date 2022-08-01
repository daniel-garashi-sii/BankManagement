package com.sii.org.il.entities;

import java.sql.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ForeignExchange {
	@JsonProperty("success")
	private boolean success;

	@JsonProperty("timestamp")
	private long timestamp;

	@JsonProperty("base")
	private String base;

	@JsonProperty("date")
	private Date date;

	@JsonProperty("rates")
	private Map<String, Double> rates;
}
