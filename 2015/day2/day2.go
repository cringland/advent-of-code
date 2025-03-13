package main

import (
	util "2015/util"
	"fmt"
	"math"
	"strconv"
	"strings"
)

type box struct {
	l int
	w int
	h int
}

func main() {
	input := util.FileLines("./day2/input")

	totalWrapping := 0
	totalRibbon := 0
	for _, i := range input {
		dims := strings.Split(i, "x")
		l, _ := strconv.Atoi(dims[0])
		w, _ := strconv.Atoi(dims[1])
		h, _ := strconv.Atoi(dims[2])
		side1 := 2 * l * w
		side2 := 2 * w * h
		side3 := 2 * h * l

		smallestArea := math.Min(math.Min(float64(side1), float64(side2)), float64(side3)) / 2
		totalWrapping += side1 + side2 + side3 + int(smallestArea)

		smallestPerimiter := math.Min(math.Min(float64(l+w), float64(w+h)), float64(h+l)) * 2
		volume := l * w * h
		totalRibbon += int(smallestPerimiter) + volume
	}

	fmt.Printf("Problem 1: %d\n", totalWrapping)
	fmt.Printf("Problem 2: %d\n", totalRibbon)
}
