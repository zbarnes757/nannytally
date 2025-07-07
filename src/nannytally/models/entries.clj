(ns nannytally.models.entries
  (:require [honey.sql :as sql]
            [next.jdbc :as jdbc]
            [next.jdbc.sql :as jdbc-sql])
  (:import [java.time LocalTime Duration]))


(defn save-entry
  "Saves a new entry record in the database"
  [conn entry-params]
  (jdbc-sql/insert! conn :time_entries entry-params {:suffix "RETURNING *"}))

(defn list-entries
  "Fetches all entries from the database"
  [conn]
  (->> {:select [:*]
        :from :time_entries
        :order-by [[:entry_date :desc]]}
       sql/format
       (jdbc/execute! conn)))

(defn add-total-hours-for-entry
  "Calculate the total hours from start to end"
  [entry]
  (let [start-time (LocalTime/parse (:time_entries/start_time entry))
        end-time (LocalTime/parse (:time_entries/end_time entry))
        duration (Duration/between start-time end-time)
        minutes (.toMinutes duration)
        total-hours (/ (double minutes) 60.0)]
    (assoc entry :total_hours total-hours)))

(defn delete-entry
  "Deletes the given entry by id"
  [conn id]
  (jdbc-sql/delete! conn :time_entries {:id id}))
