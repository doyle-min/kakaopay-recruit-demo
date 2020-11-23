package com.kakaopay.restapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BulkInsertResponse {
	private int dataCount;
	private long estimatedTimeMillies;
}
