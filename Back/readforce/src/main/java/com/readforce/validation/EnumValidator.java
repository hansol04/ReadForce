package com.readforce.validation;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEnum, String>{

	private Set<String> allowed_values;
	private boolean ignore_case;
	
	@Override
	public void initialize(ValidEnum constraint_annotation) {
		
		this.ignore_case = constraint_annotation.ignoreClass();
		
		this.allowed_values = Stream.of(constraint_annotation.enumClass().getEnumConstants())
				.map(Enum::name)
				.map(value -> ignore_case ? value.toUpperCase() : value)
				.collect(Collectors.toSet());
		
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		if(value == null || value.isBlank()) {
			
			return true;
			
		}
		
		String value_to_compare = ignore_case ? value.toUpperCase() : value;
		
		return allowed_values.contains(value_to_compare);
	
	}	
	
}
