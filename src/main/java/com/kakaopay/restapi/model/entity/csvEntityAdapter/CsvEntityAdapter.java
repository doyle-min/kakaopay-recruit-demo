package com.kakaopay.restapi.model.entity.csvEntityAdapter;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class CsvEntityAdapter<T> {

	public abstract T mapRow(String row);

	public Function<String, T> mapFunction(){
		return s -> mapRow(s);
	}

	public List<T> parseList(MultipartFile file) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
		List<T> list = reader.lines().map(mapFunction()).collect(Collectors.toList());
		return list;
	}

	public Iterator<T> iterator(MultipartFile file) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
		return reader.lines().map(mapFunction()).iterator();
	}

	public Stream<T> stream(MultipartFile file) throws IOException {
		Iterator iter = iterator(file);
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
				iter, Spliterator.ORDERED | Spliterator.NONNULL), false);
	}

}
