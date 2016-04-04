package com.mycompany.myapp.domain.patterns;

import java.math.BigDecimal;

public interface CostItem {

	public abstract void show();

	public BigDecimal CalculateTotal();
}


