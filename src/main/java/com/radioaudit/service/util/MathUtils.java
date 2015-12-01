package com.radioaudit.service.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public abstract class MathUtils {

    public static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    private static final int DEFAULT_SCALE = 2;
    private static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_EVEN;

    public static BigDecimal divide(BigDecimal dividend, BigDecimal divisor) {
        return round(notNull(dividend).divide(divisor, DEFAULT_SCALE, DEFAULT_ROUNDING_MODE).stripTrailingZeros());
    }

    public static BigDecimal multiply(BigDecimal a, BigDecimal b) {
        return round(notNull(a).multiply(notNull(b)));
    }

    public static BigDecimal calculatePercentage(BigDecimal amount, BigDecimal total) {
        return divide(notNull(amount).multiply(MathUtils.ONE_HUNDRED), notNull(total));
    }

    public static BigDecimal calculateAmountOfPercentage(BigDecimal percentage, BigDecimal total) {
        return divide(notNull(percentage).multiply(notNull(total)), MathUtils.ONE_HUNDRED);
    }

    public static boolean equalsIgnoreScale(BigDecimal a, BigDecimal b) {
        return compareTo(a, b) == 0;
    }

    public static BigDecimal normalize(BigDecimal a) {
        return a == null ? BigDecimal.ZERO : a.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    public static boolean isPositive(BigDecimal a) {
        return a == null ? false : a.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isNegative(BigDecimal a) {
        return a == null ? false : a.compareTo(BigDecimal.ZERO) < 0;
    }

    public static boolean isZero(BigDecimal a) {
        return a == null ? false : a.compareTo(BigDecimal.ZERO) == 0;
    }

    public static BigDecimal min(BigDecimal a, BigDecimal b) {
        BigDecimal a1 = round(a);
        BigDecimal b1 = round(b);
        return a1.compareTo(b1) < 0 ? a1 : b1;
    }

    public static BigDecimal max(BigDecimal a, BigDecimal b) {
        BigDecimal a1 = round(a);
        BigDecimal b1 = round(b);
        return a1.compareTo(b1) > 0 ? a1 : b1;
    }

    public static int compareTo(BigDecimal a, BigDecimal b) {
        return round(a).compareTo(round(b));
    }

    public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
        return round(notNull(a).subtract(notNull(b)));
    }

    public static BigDecimal subtractUntilZero(BigDecimal a, BigDecimal b) {
        return max(BigDecimal.ZERO, subtract(a, b));
    }

    public static BigDecimal sum(BigDecimal... d) {
        return sum(Arrays.asList(d));
    }

    public static BigDecimal sum(Collection<BigDecimal> values) {
        BigDecimal s = BigDecimal.ZERO;
        for (BigDecimal d : values) {
            if (d != null) {
                s = s.add(d);
            }
        }
        return round(s);
    }

    public static <T> Map<T, BigDecimal> prorate(Map<T, BigDecimal> values, BigDecimal targetTotal) {

        // Empty values could not be prorated
        //
        if (values == null || values.isEmpty()) {
            throw new ArithmeticException("Could not prorate empty values");
        }

        // Calcular los importes a devolver por cada concepto
        //
        BigDecimal total = sum(values.values());
        BigDecimal roundedTargetTotal = round(targetTotal);

        if (equalsIgnoreScale(roundedTargetTotal, total)) {
            return new HashMap<T, BigDecimal>(values);
        }

        // Calcular los montos devueltos por cada concepto de forma prorrateada
        //
        Map<T, BigDecimal> proratedValues = new HashMap<T, BigDecimal>(values.size());
        for (Map.Entry<T, BigDecimal> entry : values.entrySet()) {
            BigDecimal value = notNull(entry.getValue());
            BigDecimal prorated = divide(roundedTargetTotal.multiply(value), total);
            proratedValues.put(entry.getKey(), prorated);
        }

        // El prorrateo puede tener pequeñas diferencias por los redondeos, si esto pasa entonces asignamos esa
        // diferencia a alguno de los valores
        //
        BigDecimal newTotal = sum(proratedValues.values());
        BigDecimal diff = roundedTargetTotal.subtract(newTotal);
        if (!isZero(diff)) {
            Entry<T, BigDecimal> firstOne = proratedValues.entrySet().iterator().next();
            firstOne.setValue(firstOne.getValue().add(diff));
        }

        return proratedValues;
    }

    public static final BigDecimal notNull(BigDecimal a) {
        return a != null ? a : BigDecimal.ZERO;
    }

    public static BigDecimal round(BigDecimal a) {
        if (a == null) {
            return BigDecimal.ZERO;
        } else if (a.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        } else if (a.scale() > 2) {
            return a.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
        } else if (a.scale() < 0) {
            return a.setScale(0, DEFAULT_ROUNDING_MODE);
        } else {
            return a;
        }
    }

}
