package org.tree.example.optional;

import java.util.Optional;

/**
 * @author er_dong_chen
 * @date 2019年3月24日
 */
public class OptionalTest {

	// 查一个人买的车的保险的名字（人不一定有车，车也不一定买了保险）
	public static void main(String[] args) {
		Person person = new Person();
		
		// 直接跳过非空检查
		String insuranceName = Optional.ofNullable(person)
				.map(Person::getCar)
				.map(Car::getInsurance)
				.map(Insurance::getInsuranceName)
				.orElse("unknown");
		System.out.println(insuranceName);
	}

	private static class Person {
		private String name;
		private Car car;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Car getCar() {
			return car;
		}

		public void setCar(Car car) {
			this.car = car;
		}
	}

	static class Car {
		private String brand;
		private Insurance insurance;

		public String getBrand() {
			return brand;
		}

		public void setBrand(String brand) {
			this.brand = brand;
		}

		public Insurance getInsurance() {
			return insurance;
		}

		public void setInsurance(Insurance insurance) {
			this.insurance = insurance;
		}
	}

	static class Insurance {
		private String serialNumber;
		private String InsuranceName;

		public String getSerialNumber() {
			return serialNumber;
		}

		public void setSerialNumber(String serialNumber) {
			this.serialNumber = serialNumber;
		}

		public String getInsuranceName() {
			return InsuranceName;
		}

		public void setInsuranceName(String insuranceName) {
			InsuranceName = insuranceName;
		}

	}
}
