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
var ROWS int64 = 10000

func main() {
	client := redis.NewClient(&redis.Options{
		Addr:     "localhost:6379",
		Password: "",
		DB:       0,
	})
	result, err := client.Ping().Result()
	fmt.Println(result)
	fmt.Println(err)

	db, err := sqlx.Connect("mysql", "root:@tcp(10.160.0.6:9030)/demo1")
	if err != nil {
		log.Fatalf("Error opening database: %v", err)
	}
	defer db.Close()

	err = db.Ping()
	if err != nil {
		log.Fatalf("Error pinging database: %v", err)
	}
	fmt.Println("Successfully connected to the database")

	events, err := client.LRange("events", 0, int64(ROWS)).Result()

	fmt.Println(events)

	if err != nil {
		log.Fatalf("Error fetching data from Redis: %v", err)
	}

	start := time.Now()

	values := []string{}

	for _, line := range events {
		parts := strings.Split(line, "|||")
		clientID, _ := strconv.Atoi(parts[0])
		trID, _ := strconv.Atoi(parts[1])
		adate := parts[3]
		timestamp := parts[4]
		rcptEmail := parts[5]
		fromAddress := parts[6]
		msgSize, _ := strconv.Atoi(parts[7])
		remarks := parts[8]
		tokenTo := parts[9]
		msgType := parts[11]
		tags := parts[12]
		messageID := parts[13]
		openType, _ := strconv.Atoi(parts[14])

		value := fmt.Sprintf("('%d','%d','%s','%s','%s','%s','%d','%s','%s','%s','%s','%s','%d')",
			clientID, trID, adate, timestamp, rcptEmail, fromAddress, msgSize, remarks, tokenTo, msgType,
			tags, messageID, openType)
		values = append(values, value)
	}
	batch := 1000
	go_routine_no := 10
	start_row := 0
	wg.Add(go_routine_no)
	end_row := int(ROWS) / go_routine_no
	for gr := 0; gr < go_routine_no; gr++ {
		go routine(start_row, end_row, batch, db, values)
		start_row = end_row
		end_row += (int(ROWS) / go_routine_no)
	}
	wg.Wait()
	fmt.Println("Data inserted successfully")
	fmt.Println(time.Since(start).Seconds())
}

func routine(s int, e int, batch int, db *sqlx.DB, values []string) {
	for r := s; r < e; r += batch {
		end := r + batch

		if end > e {
			end = e
		}

		sqlStatement := fmt.Sprintf("INSERT INTO trans_mail_processed (clientid, trid, adate, timestamp, rcpt_email, fromaddress, msize, remarks, token_to, type, tags, message_id, open_type) VALUES %s", strings.Join(values[r:end], ","))
		execute := func(sqlStatement string) {
			_, err := db.Exec(sqlStatement)
			if err != nil {
				log.Fatalf("Error inserting data: %v", err)
			}
		}

		execute(sqlStatement)
	}
	defer wg.Done()
}

//337|||17248698010010001|||0|||20240829|||2024-08-29 14:01:34|||singhdhruv861@gmail.com|||test@m3m.in|||3184|||Message Submitted successfully|||noble-land-mermaid||||||API|||Pawnee|||7a710c3c32531480ec07db458235c80f|||0|||
