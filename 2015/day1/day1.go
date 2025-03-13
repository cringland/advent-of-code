package main

import (
	util "2015/util"
	"fmt"
)

func main() {
	input := util.FileToString("./day1/input")

	floorCount := 0
	negativeFloorIndex := -1
	for i, c := range input {
		if c == '(' {
			floorCount++
		} else {
			floorCount--
		}

		if negativeFloorIndex == -1 && floorCount == -1 {
			negativeFloorIndex = i + 1
		}
	}

	fmt.Printf("Problem 1: %d\n", floorCount)
	fmt.Printf("Problem 2: %d\n", negativeFloorIndex)
}
