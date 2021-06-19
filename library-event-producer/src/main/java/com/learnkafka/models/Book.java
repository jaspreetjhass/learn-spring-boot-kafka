package com.learnkafka.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Book {

	private Integer bookId;
	private String bookName;
	private String bookAuthor;
	
}
