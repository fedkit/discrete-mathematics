package main

import "fmt"

func MergeSeqSort(nitems int, compare func(i, j int) int, indices chan int) {
 	S := make([]chan int, nitems)
 	for i := 0; i < nitems; i++ {
  		S[i] = make(chan int)
  		go func(i int, ch chan int) {
   			ch <- i
   			close(ch)
  		}(i, S[i])
 	}

 	for len(S) > 1 {
  		var nextSeqs []chan int
  			i := 0

  		for i < (len(S)/2)*2 {
	  		out := make(chan int)
			go merge(S[i], S[i+1], compare, out)
			nextSeqs = append(nextSeqs, out)
			i += 2
		}
		if len(S)%2 == 1 {
			nextSeqs = append(nextSeqs, S[len(S)-1])
		}
		S = nextSeqs
	}

	for v := range S[0] {
		indices <- v
	}
	close(indices)
}

func merge(left, right chan int, compare func(i, j int) int, out chan int) {
	mergeChannels(left, right, compare, out)
	close(out)
}

func mergeChannels(left, right chan int, compare func(i, j int) int, out chan int) {
	l_val, lok := <-left
	r_val, rok := <-right

	for lok && rok {
		if compare(l_val, r_val) <= 0 {
			out <- l_val
			l_val, lok = <-left
		} else {
			out <- r_val
			r_val, rok = <-right
		}
	}

	for lok {
		out <- l_val	
		l_val, lok = <-left
	}

	for rok {
		out <- r_val
		r_val, rok = <-right
	}
}
func main() {
	words := []string{"quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog"}
	compare := func(i, j int) int {
		if len(words[i]) < len(words[j]) {
			return -1
		} else if len(words[i]) > len(words[j]) {
			return 1
		}
		return 0
	}
	indices := make(chan int)
	go MergeSeqSort(len(words), compare, indices)
	sortedWords := make([]string, 0, len(words))
	for index := range indices {
		sortedWords = append(sortedWords, words[index])
	}
	fmt.Println(sortedWords)
}
