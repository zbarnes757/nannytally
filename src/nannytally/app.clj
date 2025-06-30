(ns nannytally.app
  "FIXME: my new org.corfield.new/scratch project.")

(defn exec
  "Invoke me with clojure -X nannytally.app/exec"
  [opts]
  (println "exec with" opts))

(defn -main
  "Invoke me with clojure -M -m nannytally.app"
  [& args]
  (println "-main with" args))
