(ns sample.core
  (:gen-class)
  (:use [seesaw core spectator])
  (:use clojure.stacktrace)
  (:use clojure.contrib.logging)
  (:import [java.awt FlowLayout]
	   [javax.swing JFrame]
	   [org.apache.log4j BasicConfigurator]))

(defn parse-int [str]
  (try (Integer/parseInt str) 
       (catch NumberFormatException ex 0)))

(defn fahrenheit-to-celsius [f]
  (Math/round (* (- f 32) (/ 5.0 9))))

(defn celsius-to-fahrenheit [c]
  (Math/round (+ (* c (/ 9.0 5)) 32)))

(defn -main []
  (BasicConfigurator/configure)
  (use-system-look-and-feel)
  (let [context (make-context)
	frame (frame :title "Seesaw Sample")
	celsius (text-field context :celsius :columns 5)
	fahrenheit (text-field context :fahrenheit :columns 5)
	celsius-label (label :text "Celsius:")
	fahrenheit-label (label :text "Fahrenheit:")]
    (add-updater! context (fn [old new]
			    {:celsius (str (fahrenheit-to-celsius
					    (parse-int (:fahrenheit new))))})
		  :fahrenheit)
    (add-updater! context (fn [old new]
			    {:fahrenheit (str (celsius-to-fahrenheit
					       (parse-int (:celsius new))))})
		  :celsius)
    (prepare-frame frame (FlowLayout.)
		   fahrenheit-label
		   fahrenheit
		   celsius-label
		   celsius)))
    