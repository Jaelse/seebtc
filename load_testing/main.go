package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"math/rand"
	"net/http"
	"os"
	"os/signal"
	"strconv"
	"sync"
	"syscall"
	"time"
)

var stopFlag bool

func main() {
	stopFlag = false // Initialize stop flag to false

	// Check if wallet ID is provided as an argument
	if len(os.Args) < 2 {
		fmt.Println("Usage: go run main.go <wallet_id>")
		return
	}

	stopper()
	walletID := os.Args[1] // Get the wallet ID from the command-line argument

	numSenders, err := strconv.Atoi(os.Args[2]) // Get the number of senders from the command-line argument
	if err != nil || numSenders <= 0 {
		fmt.Println("Invalid number of senders")
		return
	}

	// List of time zones
	timeZones := []string{"America/New_York", "Europe/London", "Asia/Tokyo"}

	// Create a ticker to send a request every second
	ticker := time.NewTicker(time.Second)

	var wg sync.WaitGroup

	// Launch senders
	for i := 0; i < numSenders; i++ {
		r := rand.Intn(10)
		time.Sleep(time.Duration(r) * time.Microsecond)

		wg.Add(1)
		go func() {
			defer wg.Done()
			for {
				select {
				case <-ticker.C:

					if stopFlag {
						fmt.Println("Sender will stop sending BTC")
						return // If stop flag is true, stop sending requests
					}

					// Get a random time zone from the list
					randomIndex := rand.Intn(len(timeZones))
					targetTimeZone := timeZones[randomIndex]

					// Get the current time in the target time zone
					currentTime := time.Now().In(getTimeZone(targetTimeZone))

					// Generate a random amount
					amount := rand.Float64() * 10

					// Create the request body
					requestBody := map[string]interface{}{
						"datetime": currentTime.Format(time.RFC3339),
						"amount":   fmt.Sprintf("%.2f", amount),
					}
					jsonBody, err := json.Marshal(requestBody)
					if err != nil {
						fmt.Println("Failed to marshal request body:", err)
						continue
					}

					// Send the HTTP POST request
					response, err := http.Post("http://localhost:8080/v1/wallets/"+walletID+"/transfer", "application/json", bytes.NewBuffer(jsonBody))
					if err != nil {
						fmt.Println("Failed to send request:", err)
						continue
					}
					defer response.Body.Close()

					// Check the response status code
					if response.StatusCode != http.StatusCreated {
						fmt.Println("Request failed with status code:", response.StatusCode)
						continue
					}

					fmt.Println("Request sent successfully at", currentTime.Format(time.RFC3339))

				}
			}
		}()
	}

	wg.Wait()
}

func getTimeZone(timezone string) *time.Location {
	location, err := time.LoadLocation(timezone)
	if err != nil {
		fmt.Println("Failed to load time zone:", err)
		return time.UTC
	}
	return location
}

func stopper() {
	// Handle signals to stop the goroutines
	sigchan := make(chan os.Signal, 1)
	signal.Notify(sigchan, syscall.SIGINT, syscall.SIGTERM)
	go func() {
		<-sigchan
		stopFlag = true // Set stop flag to true when a signal is received
	}()
}
