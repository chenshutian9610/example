package org.tree.example.design;

import java.util.function.DoubleBinaryOperator;

/**
 * @author er_dong_chen
 * @date 2019年3月24日
 * 
 * 不要在枚举中使用 switch，取而代之可以使用以下方式来实现不同的行为
 */
public enum OperationEnum {
    PLUS((x, y) -> x + y),
    SUBTRACT((x, y) -> x - y),
    MULTIPLY((x, y) -> x * y),
    DIVIDE((x, y) -> x / y);

    private DoubleBinaryOperator operator;

    OperationEnum(DoubleBinaryOperator operator) {
        this.operator = operator;
    }

    public double apply(double x, double y) {
		return operator.applyAsDouble(x, y);
	}
}
