package com.readforce.validation;

import java.time.LocalDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BirthdayValidator implements ConstraintValidator<ValidBirthday, LocalDate>{

	@Override
	public boolean isValid(LocalDate birthday, ConstraintValidatorContext context) {
		
		if(birthday == null) {
			return true;
		}

		final LocalDate min_date = LocalDate.of(1900, 1, 1);
		final LocalDate max_date = LocalDate.now().minusYears(3);
		
		return !birthday.isBefore(min_date) && !birthday.isAfter(max_date);
	}

	
	
}
