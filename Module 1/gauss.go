package main

import (
	"fmt"
)

type Fraction struct {
	Numerator   int
	Denominator int
}

func gcd(a, b int) int {
	for b != 0 {
		a, b = b, a%b
	}
	return a
}

func addFraction(f1, f2 Fraction) Fraction {
	var denom, num, g int
	denom = f1.Denominator * f2.Denominator
	num = f1.Numerator*f2.Denominator + f2.Numerator*f1.Denominator
	g = gcd(num, denom)
	if g == 0 {
		return Fraction{num, denom}
	}
	return Fraction{num / g, denom / g}
}

func minusFraction(f1, f2 Fraction) Fraction {
	var denom, num, g int
	denom = f1.Denominator * f2.Denominator
	num = f1.Numerator*f2.Denominator - f2.Numerator*f1.Denominator
	g = gcd(num, denom)
	if g == 0 {
		return Fraction{num, denom}
	}
	return Fraction{num / g, denom / g}
}

func multiplyFraction(f1, f2 Fraction) Fraction {
	var num, denom, g int
	num = f1.Numerator * f2.Numerator
	denom = f1.Denominator * f2.Denominator
	g = gcd(num, denom)
	if g == 0 {
		return Fraction{num, denom}
	}
	return Fraction{num / g, denom / g}
}

func backwardSubstitution(n int, matrix [][]int) []Fraction {
	results := make([]Fraction, n)
	for i := n - 1; i >= 0; i-- {
		var rightSide Fraction
		rightSide.Numerator = matrix[i][n]
		rightSide.Denominator = 1
		for j := i + 1; j < n; j++ {
			term := multiplyFraction(results[j], Fraction{matrix[i][j], 1})
			rightSide = minusFraction(rightSide, term)
		}
		results[i] = multiplyFraction(rightSide, Fraction{1, matrix[i][i]})
	}

	return results
}

func gaussianElimination(n int, matrix [][]int) bool {
	for i := 0; i < n-1; i++ {
		var k int
		for j := i; j < n; j++ {
			if matrix[j][i] != 0 {
				k = j
				break
			}
		}
		if matrix[k][i] == 0 {
			return false
		}
		matrix[i], matrix[k] = matrix[k], matrix[i]
	}
	for i := 0; i < n; i++ {
		pivot := matrix[i][i]
		for j := i + 1; j < n; j++ {
			factor := gcd(pivot, matrix[j][i])
			var k1, k2 int
			if factor != 0 {
				k1 = pivot / factor
				k2 = matrix[j][i] / factor
			} else {
				k1 = pivot
				k2 = matrix[j][i]
			}
			for k := i; k < n+1; k++ {
				matrix[j][k] = matrix[j][k]*k1 - matrix[i][k]*k2
			}
		}
	}
	return true
}

func inputSystem(n int, mas [][]int) {
	for i := 0; i < n; i++ {
		mas[i] = make([]int, n+1)
		for j := 0; j < n+1; j++ {
			fmt.Scan(&mas[i][j])
		}
	}
}

func checkZeroCol(n int, mas [][]int) bool {
	for i := 0; i < n+1; i++ {
		var k int
		for j := 0; j < n; j++ {
			if mas[j][i] == 0 {
				k++
			}
		}
		if k == n {
			return true
		}
	}
	return false
}

func main() {
	var n int
	fmt.Scanln(&n)
	mas := make([][]int, n)
	inputSystem(n, mas)
	if checkZeroCol(n, mas) {
		fmt.Println("No solution")
	} else {
		if gaussianElimination(n, mas) {
			results := backwardSubstitution(n, mas)
			for i := 0; i < n; i++ {
				if results[i].Denominator < 0 {
					fmt.Printf("%d/%d\n", -1*results[i].Numerator, 
					                       -1*results[i].Denominator)
				} else if results[i].Denominator == 0 {
					fmt.Println("No solution")
					break
				} else {
					fmt.Printf("%d/%d\n", results[i].Numerator,     
					                        results[i].Denominator)
				}
			}
		} else {
			fmt.Println("No solution")
		}
	}
}
