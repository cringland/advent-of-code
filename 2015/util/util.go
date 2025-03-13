package util

import (
	"os"
	"strings"
)

func FileToString(path string) string {
	dat, err := os.ReadFile(path)
	if err != nil {
		panic(err)
	}
	return string(dat)
}

func FileLines(path string) []string {
	return strings.Split(FileToString(path), "\n")
}
