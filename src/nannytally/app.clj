(ns nannytally.app
  "Logging and Tracking hours for our nanny, Ju"
  (:require
   [integrant.core :as ig]
   [nannytally.system :refer [system-map]])
  (:gen-class))


(defn start! [_opts] (ig/init system-map))

(defn -main
  "Invoke me with clojure -M -m nannytally.app"
  [& _args]
  (start! {:port 3000 :join? true}))
