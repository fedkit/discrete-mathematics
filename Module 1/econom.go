package main

import (
	"bufio"
	"fmt"
	"os"
)

func isLetter(tok byte) bool {
	return 'a' <= tok && tok <= 'z'
}

func isAction(tok byte) bool {
	return tok == '$' || tok == '#' || tok == '@'
}

type Stack struct {
	inf []int
}

func (s *Stack) Push(val int) {
	s.inf = append(s.inf, val)
}

func (s *Stack) Pop() int {
	index := len(s.inf) - 1
	val := s.inf[index]
	s.inf = s.inf[:index]
	return val
}

func econom(s string) int {
	stack := Stack{}
	counter := make(map[string]int)
	for i, val := range s {
		if isAction(byte(val)) {
			stack.Push(i)
		} else if val == '👺' {
			n := len(stack.inf)
			expression := s[stack.inf[n-1] : i+1]
			counter[expression]++
			stack.Pop()
		}
	}
	return len(counter)
}

func main() {
	expr, _ := bufio.NewReader(os.Stdin).ReadString('\n')
	var s string
	for i := 0; i < len(expr); i++ {
		if isLetter(expr[i]) || isAction(expr[i]) {
			s += string(expr[i])
		} else if expr[i] == ')' {
			s += string('👺')
		}
	}
	if len(s) <= 4 {
		fmt.Println(0)
	} else {
		result := econom(s)
		fmt.Println(result)
	}
}
