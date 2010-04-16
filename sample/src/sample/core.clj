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
  (javax.swing.UIManager/setLookAndFeel (javax.swing.UIManager/getSystemLookAndFeelClassName))
  (let [context (make-context)
	frame (frame "Seesaw Sample")
	celsius (textfield context :celsius 5)
	fahrenheit (textfield context :fahrenheit 5)
	celsius-label (label "Celsius:")
	fahrenheit-label (label "Fahrenheit:")]
    (add-updater! context (fn [old new]
			    {:celsius (str (fahrenheit-to-celsius (parse-int (:fahrenheit new))))})
		  :fahrenheit)
    (add-updater! context (fn [old new]
			    {:fahrenheit (str (celsius-to-fahrenheit (parse-int (:celsius new))))})
		  :celsius)
    (doto frame
      (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE)
      (.setLayout (FlowLayout.))
      (.add fahrenheit-label)
      (.add fahrenheit)
      (.add celsius-label)
      (.add celsius)
      .pack
      .show)))
    