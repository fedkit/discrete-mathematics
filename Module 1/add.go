package main

import "fmt"

func add(x, y []int32, p int32) []int32 {
	if len(x) == 1 && len(y) == 1 && x[0]+y[0] < p {
		x[0] = x[0] + y[0]
	} else if len(x) == 1 && len(y) == 1 && x[0]+y[0] >= p {
		x[0] = (x[0] + y[0]) % p
		x = append(x, 1)
	} else {
		var buf int32 = 0
		var res []int32
		max_len := len(x)
		if len(y) > max_len {
			max_len = len(y)
		}
		for i := 0; i < max_len; i++ {
			var sum int32
			if i < len(x) {
				sum += x[i]
			}
			if i < len(y) {
				sum += y[i]
			}
			sum += buf
			buf = sum / p
			res = append(res, sum%p)
		}
		if buf > 0 {
			res = append(res, buf)
		}
		x = res
	}
	return x
}

func main() {
	digit1 := []int32{1, 2}
	digit2 := []int32{1, 2}
	var p int32 = 3
	fmt.Println(add(digit1, digit2, p))
}
