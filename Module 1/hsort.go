package main

import "fmt"

func heapify(n, index int,
	less func(i, j int) bool,
	swap func(i, j int)) {
	largest := index
	left := index * 2 + 1
	right := index * 2 + 2
	if left < n {
		if (right >= n || less(right, left)) && less(index, left) {
			largest = left
		}
	}
	if right < n && less(largest, right) {
		largest = right
	}
	if (largest != index) && (index*2+1 < n) {
		swap(index, largest)
		heapify(n, largest, less, swap)
	}
}


func hsort(n int,
	      less func(i, j int) bool,
	      swap func(i, j int)) {
	for i := n - 1; i >= 0; i-- {
		heapify(n, i, less, swap)
	}
	for i := n - 1; i >= 0; i-- {
		swap(i, 0)
		heapify(i, 0, less, swap)
	}
}

func main() {
	mas := []int{5, 9, -13, 16, 12, 0, -3, 1}
	hsort(len(mas),
		func(x, y int) bool {
			return mas[x] < mas[y]
		},
		func(i, j int) {
			mas[i], mas[j] = mas[j], mas[i]
		})
	for i := 0; i < len(mas); i++ {
		fmt.Print(mas[i], " ")
	}
}
