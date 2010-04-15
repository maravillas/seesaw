(ns sample.core
  (:gen-class)
  (:use [seesaw core spectator])
  (:use clojure.stacktrace)
  (:use clojure.contrib.logging)
  (:import [java.awt FlowLayout]
	   [javax.swing JFrame]
	   [org.apache.log4j BasicConfigurator]))

(defn parse-float [str]
  (try (Float/parseFloat str) 
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
	c-check (checkbox context :celsius)
	f-check (checkbox context :fahrenheit)]
    (add-updater! context (fn [old new]
			    {:celsius (:fahrenheit new)})
			    ;{:celsius (fahrenheit-to-celsius (:fahrenheit new))})
		  :fahrenheit)
    (add-updater! context (fn [old new]
			    {:fahrenheit (:celsius new)})
			    ;{:fahrenheit (celsius-to-fahrenheit (:celsius new))})
		  :celsius)
    (doto frame
      (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE)
      (.setLayout (FlowLayout.))
      (.add f-check)
      (.add c-check)
      .pack
      .show)))
    