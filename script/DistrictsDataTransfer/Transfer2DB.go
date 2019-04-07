package main

import (
	"context"
	"database/sql"
	"fmt"
	"log"
	"strconv"
	"strings"
	"time"

	"github.com/lib/pq"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"

	_ "github.com/lib/pq"
	"github.com/tealeg/xlsx"
)

type District struct {
	ID           int64   `db:"i_id"`
	SupervisorID int64   `db:"i_supervisor_id"`
	Path         []int64 `db:"ia_path_root"`
	Level        int64   `db:"i_level"`
	Name         string  `db:"ch_name"`
	Province     string  `db:"ch_province_name"`
	City         string  `db:"ch_city_name"`
	County       string  `db:"ch_county_name"`
}
type DistrictInterface interface {
}

func main() {
	excelFileName := "districts.xlsx"
	xlFile, err := xlsx.OpenFile(excelFileName)
	if err != nil {
		log.Fatal("Cannot open xlsx file. err = %v", err)
	}

	districts := InitPostgresDB(xlFile)
	InitMongoDB(districts)
}

func InitPostgresDB(xlFile *xlsx.File) (districts []interface{}) {
	// Prepare PostgreSQL client.
	connStr := `dbname=postgres user=postgres password=*6Z,<[9A97Mp;6W sslmode=disable`
	pgdb, err := sql.Open("postgres", connStr)
	if err != nil {
		log.Fatal("Connot connect to PostgreSQL. err = %v", err)
	}

	txn, err := pgdb.Begin()
	if err != nil {
		log.Fatal(err)
	}
	stmt, err := txn.Prepare(pq.CopyIn("b_districts", "i_id", "i_supervisor_id", "ch_path_root", "i_level", "ch_name", "ch_province_name", "ch_city_name", "ch_county_name"))
	if err != nil {
		log.Fatal(err)
	}

	// 读取xlsx并存入两数据库
	for _, sheet := range xlFile.Sheets {
		for i, row := range sheet.Rows {
			if i == 0 { // 跳过表头
				continue
			}
			cells := row.Cells
			id, _ := strconv.ParseInt(cells[0].String(), 10, 64)
			super, _ := strconv.ParseInt(cells[1].String(), 10, 64)
			level, _ := strconv.ParseInt(cells[3].String(), 10, 64)
			path := []int64{}
			for _, v := range strings.Split(cells[2].String(), ",") {
				value, _ := strconv.ParseInt(v, 10, 64)
				path = append(path, value)
			}

			tmpDistrict := District{
				ID:           id,
				SupervisorID: super,
				Path:         path,
				Name:         cells[4].String(),
				Level:        level,
				Province:     cells[7].String(),
				City:         cells[10].String(),
				County:       cells[13].String(),
			}
			log.Println("Row data parsed: ", tmpDistrict)
			districts = append(districts, &tmpDistrict)

			_, err := stmt.Exec(tmpDistrict.ID, tmpDistrict.SupervisorID, fmt.Sprintf("{%s}", cells[2].String()), tmpDistrict.Level, tmpDistrict.Name, tmpDistrict.Province, tmpDistrict.City, tmpDistrict.County)
			if err != nil {
				log.Println("One row failed. err = %v", err)
			}

		}
	}

	_, err = stmt.Exec()
	if err != nil {
		log.Fatal(err)
	}

	err = stmt.Close()
	if err != nil {
		log.Fatal(err)
	}

	err = txn.Commit()
	if err != nil {
		log.Fatal(err)
	}
	return
}

func InitMongoDB(districts []interface{}) {
	// Prepare MongoDB client
	mongoOptions := options.Client().ApplyURI("mongodb://localhost:27017").SetAuth(options.Credential{
		Username: "root",
		Password: "iCOVX-Gj7WqlH4Z",
	})
	mongocli, err := mongo.NewClient(mongoOptions)
	if err != nil {
		log.Fatal("Cannot connect to mongodb. err = %v", err)
	}
	ctx, _ := context.WithTimeout(context.Background(), time.Minute*3)
	err = mongocli.Connect(ctx)
	if err != nil {
		log.Fatal("Cannot connect to mongodb. err = %v", err)
	}
	mongodb := mongocli.Database("criminalquery")

	result, err := mongodb.Collection("districts").InsertMany(ctx, districts)
	if err != nil {
		log.Println("Insert into mongodb failed. err = %v", err)
	}
	log.Println(result.InsertedIDs)

	mongocli.Disconnect(ctx)
}
