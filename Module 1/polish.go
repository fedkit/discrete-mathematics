package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
)

func isDigit(tok byte) bool {
	return '0' <= tok && tok <= '9'
}

func isAction(tok byte) bool {
	return tok == '+' || tok == '-' || tok == '*'
}

func polish(s []string) int {
	var number []int
	for len(s) > 0 {
		if isDigit(([]byte(s[len(s)-1]))[0]) {
			a, _ := strconv.Atoi(s[len(s)-1])
			number = append(number, a)
		}
		if isAction(([]byte(s[len(s)-1]))[0]) {
			a1 := number[len(number)-1]
			a2 := number[len(number)-2]
			number = number[:len(number)-1]
			if ([]byte(s[len(s)-1]))[0] == '+' {
				number[len(number)-1] = a2 + a1
			} else if ([]byte(s[len(s)-1]))[0] == '-' {
				number[len(number)-1] = a1 - a2
			} else if ([]byte(s[len(s)-1]))[0] == '*' {
				number[len(number)-1] = a2 * a1
			}
		}
		s = s[:len(s)-1]
	}
	return number[0]
}

func main() {
	expr, _ := bufio.NewReader(os.Stdin).ReadString('\n')
	var s []string
	for i := 0; i < len(expr); i++ {
		if isDigit(expr[i]) || isAction(expr[i]) {
			s = append(s, string(expr[i]))
		}
	}
	result := polish(s)
	fmt.Println(result)
}
