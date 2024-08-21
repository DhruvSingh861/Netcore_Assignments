package main

import (
	"fmt"
	"log"
	"strconv"
	"strings"
	"sync"
	"time"

	"github.com/go-redis/redis"
	_ "github.com/go-sql-driver/mysql"
	"github.com/jmoiron/sqlx"
)

var wg sync.WaitGroup

func main() {
	var ROWS int64 = 1000000
	client := redis.NewClient(&redis.Options{
		Addr:     "localhost:6379",
		Password: "",
		DB:       0,
	})
	result, err := client.Ping().Result()
	fmt.Println(result)
	fmt.Println(err)

	db, err := sqlx.Connect("mysql", "root:@tcp(10.160.0.6:9030)/demo")
	if err != nil {
		log.Fatalf("Error opening database: %v", err)
	}
	defer db.Close()

	err = db.Ping()
	if err != nil {
		log.Fatalf("Error pinging database: %v", err)
	}
	fmt.Println("Successfully connected to the database")

	events, err := client.LRange("events", 0, ROWS).Result()
	if err != nil {
		log.Fatalf("Error fetching data from Redis: %v", err)
	}

	start := time.Now()

	values := []string{}

	for _, line := range events {
		parts := strings.Split(line, "|||")
		clientID, _ := strconv.Atoi(parts[0])
		trID, _ := strconv.Atoi(parts[1])
		adate := parts[2]
		atime := parts[3]
		batchID := parts[4]
		customData := parts[5]
		fromAddress := parts[6]
		fromAddressID := parts[7]
		injType := parts[8]
		itime := parts[9]
		misc1 := parts[10]
		msgSize, _ := strconv.Atoi(parts[11])
		rcptEmail := parts[12]
		rcptID := parts[13]
		rdom := parts[14]
		schTime := parts[15]
		schStatus, _ := strconv.Atoi(parts[16])
		subject := parts[17]
		tags := parts[18]
		udate := parts[19]
		remark := parts[20]
		openType, _ := strconv.Atoi(parts[21])

		value := fmt.Sprintf("('%d','%d','%s','%s','%s','%s','%s','%s','%s','%s','%s','%d','%s','%s','%s','%s','%d','%s','%s','%s','%s','%d')",
			clientID, trID, adate, atime, batchID, customData, fromAddress, fromAddressID,
			injType, itime, misc1, msgSize, rcptEmail, rcptID, rdom, schTime, schStatus,
			subject, tags, udate, remark, openType)
		values = append(values, value)
		// fmt.Println(value)
	}
	batch := 10000

	for i := 0; i < len(events); i += batch {
		end := i + batch

		if end > len(events) {
			end = len(events)
		}

		sqlStatement := fmt.Sprintf("INSERT INTO trans_mail_processed (clientid, trid, adate, atime, batch_id, custom_data, fromaddress, fromaddressid, inj_type, itime, misc1, msize, rcpt_email, rcptid, rdom, sch_time, sch_status, subject, tags, udate, remarks, open_type) VALUES %s", strings.Join(values[i:end], ","))

		wg.Add(1)
		execute := func(sqlStatement string) {
			_, err := db.Exec(sqlStatement)
			if err != nil {
				log.Fatalf("Error inserting data: %v", err)
			}
			defer wg.Done()
		}

		go execute(sqlStatement)
	}
	wg.Wait()
	fmt.Println("Data inserted successfully")
	fmt.Println(time.Since(start).Seconds())
}
