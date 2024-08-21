package main

import (
	"fmt"
	"math/rand"
	"time"

	"github.com/go-redis/redis"
)

func randomString(length int) string {
	chars := "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
	result := make([]byte, length)
	for i := range result {
		result[i] = chars[rand.Intn(len(chars))]
	}
	return string(result)
}

func randomDate() string {
	min := time.Date(2023, 1, 1, 0, 0, 0, 0, time.UTC).Unix()
	max := time.Now().Unix()
	seconds := rand.Int63n(max-min) + min
	return time.Unix(seconds, 0).Format("2006-01-02")
}

func randomDateTime() string {
	min := time.Date(2023, 1, 1, 0, 0, 0, 0, time.UTC).Unix()
	max := time.Now().Unix()
	seconds := rand.Int63n(max-min) + min
	return time.Unix(seconds, 0).Format("2006-01-02 15:04:05")
}

func main() {
	client := redis.NewClient(&redis.Options{
		Addr:     "localhost:6379",
		Password: "",
		DB:       0,
	})
	result, err := client.Ping().Result()
	fmt.Println(result)
	if err != nil {
		fmt.Println(err)
	}

	clientID := 175440
	trID := 17225379745204241
	batchID := "100"
	msgSize := 6075
	remark := "this is remark"
	fromAddress := "info@mavoyanceceleste.net"
	fromAddressID := "m3m.in"
	rcptEmail := "ayack@outlook.fr"
	rcptID := "fgsjdh"
	rdom := "outlook.fr"
	customData := "this is custom data"
	injType := "SMTP"
	misc1 := "xyz"
	subject := "test mail"
	tags := "{}"
	openType := rand.Intn(2)
	schStatus := rand.Intn(3)
	client.Del("events")

	for i := 0; i <= 10000; i++ {
		entry := fmt.Sprintf("%d|||%d|||%s|||%s|||%s|||%s|||%s|||%s|||%s|||%s|||%s|||%d|||%s|||%s|||%s|||%s|||%d|||%s|||%s|||%s|||%s|||%d",
			clientID, trID, randomDate(), randomDateTime(), batchID, customData, fromAddress, fromAddressID,
			injType, randomDateTime(), misc1, msgSize, rcptEmail, rcptID, rdom, randomDateTime(), schStatus,
			subject, tags, randomDateTime(), remark, openType)

		client.LPush("events", entry)

		// fmt.Println(entry, "\n")

		clientID++
		trID++
		msgSize++
	}
}
