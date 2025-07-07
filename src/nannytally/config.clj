(ns nannytally.config
  (:require
   [aero.core :as aero]
   [clojure.java.io :as io]
   [clojure.tools.logging :as logging]))

(def get-profile
  (memoize (fn []
             (logging/info "Reading PROFILE from env")
             (keyword (System/getenv "PROFILE")))))

(defn read-config []
  (aero/read-config (io/resource "config.edn") {:profile (get-profile)}))
