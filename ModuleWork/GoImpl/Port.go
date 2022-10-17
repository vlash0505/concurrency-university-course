package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

const (
	MAX_CAPACITY int    = 10
	PASS         string = "pass"
	LOAD                = "load"
)

type Terminal struct {
	currentCapacity int
	mutex           *sync.Mutex
	condition       *sync.Cond
}

func addContainer(terminal *Terminal) {
	terminal.mutex.Lock()
	if terminal.currentCapacity+1 > MAX_CAPACITY {
		unlock(terminal)
		return
	}
	terminal.currentCapacity++
	fmt.Println("Added a container to the terminal")
	unlock(terminal)
}

func removeContainer(terminal *Terminal) {
	terminal.mutex.Lock()
	if terminal.currentCapacity-1 < 0 {
		unlock(terminal)
		return
	}
	terminal.currentCapacity--
	fmt.Println("Removed a container from the terminal")
	unlock(terminal)
}

func unlock(terminal *Terminal) {
	terminal.condition.Broadcast()
	terminal.mutex.Unlock()
}

func initTerminal(terminal *Terminal) {
	for {
		var taskType = ""
		var randomValue = rand.Intn(2)
		if randomValue == 0 {
			taskType = LOAD
		} else {
			taskType = PASS
		}

		if taskType == PASS {
			addContainer(terminal)
		} else {
			removeContainer(terminal)
		}
		time.Sleep(2 * time.Second)
	}
}

func main() {
	lock := sync.Mutex{}
	condition := sync.NewCond(&lock)
	terminal := Terminal{currentCapacity: 0, mutex: &lock, condition: condition}
	//two terminals are operating in the port
	go initTerminal(&terminal)
	go initTerminal(&terminal)

	time.Sleep(2 * time.Minute)
}
