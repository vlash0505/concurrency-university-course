package main

import (
	"fmt"
	"math/rand"
)

var templeCapacity = 10
var strongestMagician = 0

func fillRandom(arr [10]int) [10]int {
	for i := 0; i < len(arr); i++ {
		arr[i] = rand.Intn(100) + 1
	}
	return arr
}

func fight(first, second int) {
	fmt.Println(first, " fights ", second)
	if first < strongestMagician && second < strongestMagician {
		return
	}
	if first <= second {
		strongestMagician = second
	} else {
		strongestMagician = first
	}
}

func main() {
	firstTemple := [10]int{}
	secondTemple := [10]int{}
	firstTemple = fillRandom(firstTemple)
	secondTemple = fillRandom(secondTemple)

	for i := 0; i < templeCapacity; i++ {
		go fight(firstTemple[i], secondTemple[i])
		fight(firstTemple[i], secondTemple[i])
	}
	fmt.Println("Strongest magician has ", strongestMagician, " energy")
}
