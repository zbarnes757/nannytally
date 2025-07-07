(ns nannytally.views
  (:require [hiccup.page :as h])
  (:import [java.time LocalDate]
           [java.time.format DateTimeFormatter]))

(def human-readable-formatter (DateTimeFormatter/ofPattern "E, MMM d"))

(defn format-date [date-str]
  (-> date-str
      LocalDate/parse
      (.format human-readable-formatter)))

(defn layout [& body]
  (h/html5
   [:head
    [:meta {:charset "UTF-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
    [:title "NannyTally"]
    [:link {:rel "stylesheet" :href "https://cdn.jsdelivr.net/npm/@picocss/pico@2/css/pico.min.css"}]
    [:script {:src "https://cdn.jsdelivr.net/npm/htmx.org@2.0.6/dist/htmx.min.js"
              :integrity "sha384-Akqfrbj/HpNVo8k11SXBb6TlBWmXXlYQrCSqEWmyKJe+hDm3Z/B2WVG4smwBkRVm"
              :crossorigin "anonymous"}]
    [:style "main.container {padding-top: 2rem; padding-bottom: 2rem;} .weekly-summary {text-align: right; font-size: 1.2em; margin-top: 1rem;}"]]
   [:body
    [:main.container
     [:hgroup
      [:h1 "NannyTally"]
      [:h2 "Tracking hours for our nanny, Ju"]]
     [:div#main-content body]]]))

(defn entry-row [entry]
  [:tr
   [:td (format-date (:time_entries/entry_date entry))]
   [:td (:time_entries/start_time entry)]
   [:td (:time_entries/end_time entry)]
   [:td [:strong (format "%.2f" (or (:total_hours entry) 0.0))]] ;; placeholder for calculated hours
   [:td (:time_entries/notes entry)]
   [:td
    [:button.secondary {:hx-delete (str "/entries/" (:time_entries/id entry))} "Delete"]]])

(defn sum-total-entry-hours [entries]
  (->> entries
       (map :total_hours)
       (reduce +)))

(defn entries-table [entries]
  [:article#entries-article
   [:header [:h3 "This Week's Hours"]]
   [:figure
    [:table {:hx-target "closest tr" :hx-swap "outerHTML"}
     [:thead
      [:tr
       [:th "Date"] [:th "Start"] [:th "End"] [:th "Total"] [:th "Notes"] [:th ""]]]
     [:tbody#entries-list
      (for [entry entries]
        (entry-row entry))]]]
   [:div#weekly-summary.weekly-summary
    [:strong (format "Total This Week: %.2f hours" (sum-total-entry-hours entries))]]])

(defn index-page [entries]
  (layout
   [:article
    [:header [:h3 "Add New Entry"]]
    [:form {:hx-post "/entries"
            :hx-target "#entries-article"
            :hx-swap "outerHTML"
            "hx-on::after-request" "this.reset()"}
     [:div.grid
      [:label {:for "entry_date"} "Date"
       [:input {:type "date", :id "entry_date", :name "entry_date", :required true}]]
      [:label {:for "start_time"} "Start Time"
       [:input {:type "time", :id "start_time", :name "start_time", :required true}]]
      [:label {:for "end_time"} "End Time"
       [:input {:type "time", :id "end_time", :name "end_time", :required true}]]]
     [:label {:for "notes"} "Notes"]
     [:input {:type "text", :id "notes", :name "notes", :placeholder "e.g., Stayed late"}]
     [:button {:type "submit"} "Add Entry"]]]

   (entries-table entries)))
