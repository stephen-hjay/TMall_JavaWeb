

package com.hjay.tmall.comparator;

import com.hjay.tmall.Entity.Implement.Product;

import java.util.Comparator;


public class ProductPriceComparator implements Comparator<Product>{

	@Override
	public int compare(Product p1, Product p2) {
		return (int) (p1.getPromotePrice()-p2.getPromotePrice());
	}

}


